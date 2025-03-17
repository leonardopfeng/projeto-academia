import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './ExercisesForm.css';

const ExercisesForm = () => {
  const { sessionId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [exercises, setExercises] = useState({});
  const [selectedExercises, setSelectedExercises] = useState([]);
  const [exerciseDetails, setExerciseDetails] = useState({});
  const [expandedGroups, setExpandedGroups] = useState({});
  const [groupNames, setGroupNames] = useState({});
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);
  const [groupedExercises, setGroupedExercises] = useState([]);

  useEffect(() => {
    fetchData();
  }, [sessionId]);

  const fetchData = async () => {
    setLoading(true);
    setError(null);

    try {
      const accessToken = localStorage.getItem('accessToken');
      if (!accessToken) {
        setError('No access token found. Please log in again.');
        setLoading(false);
        return;
      }

      const headers = {
        'Authorization': `Bearer ${accessToken}`
      };

      // Fetch exercises grouped by their groups using the new backend endpoint
      const groupedResponse = await api.get('/api/exercise/v1/grouped', { headers });
      console.log('Grouped exercises response:', groupedResponse.data);
      
      if (groupedResponse.data && Array.isArray(groupedResponse.data)) {
        // Initialize expanded state for all groups
        const initialExpandedState = {};
        groupedResponse.data.forEach(group => {
          initialExpandedState[group.id] = true; // Start with all groups expanded
        });
        setExpandedGroups(initialExpandedState);
        
        // Set the grouped exercises directly
        setGroupedExercises(groupedResponse.data);
      } else {
        console.error('Invalid response format for grouped exercises:', groupedResponse.data);
        setError('Invalid data format received from server.');
      }

      // Fetch existing exercises for this session
      try {
        const sessionExercisesResponse = await api.get(`/api/sessionExercise/v1/${sessionId}`, { headers });
        const sessionExercises = sessionExercisesResponse.data?._embedded?.sessionExerciseVOList || [];
        
        // Find the corresponding exercises and set them as selected
        const selectedExs = [];
        const details = {};
        
        sessionExercises.forEach(sessionExercise => {
          const exerciseId = sessionExercise.id.exerciseId.toString();
          selectedExs.push({ key: exerciseId });
          
          details[exerciseId] = {
            sequence: sessionExercise.sequence,
            sets: sessionExercise.sets,
            reps: sessionExercise.reps,
            weight: sessionExercise.weight
          };
        });
        
        setSelectedExercises(selectedExs);
        setExerciseDetails(details);
      } catch (error) {
        console.error('Error fetching session exercises:', error);
        // Continue showing the form even if fetching existing exercises fails
      }
    } catch (error) {
      console.error('Error fetching data:', error);
      if (error.response && error.response.status === 401) {
        setError('Your session has expired. Please log in again.');
      } else {
        setError('Failed to load data. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const toggleGroup = (group) => {
    setExpandedGroups(prev => ({
      ...prev,
      [group]: !prev[group]
    }));
  };

  const toggleExercise = (exerciseId) => {
    const exerciseKey = exerciseId.toString();
    const isSelected = isExerciseSelected(exerciseId);
    
    if (isSelected) {
      // Remove exercise
      setSelectedExercises(prev => prev.filter(e => e.key !== exerciseKey));
      // Remove details
      setExerciseDetails(prev => {
        const newDetails = { ...prev };
        delete newDetails[exerciseKey];
        return newDetails;
      });
    } else {
      // Add exercise with default details
      setSelectedExercises(prev => [...prev, { key: exerciseKey }]);
      setExerciseDetails(prev => ({
        ...prev,
        [exerciseKey]: {
          sequence: prev[exerciseKey]?.sequence || 1,
          sets: prev[exerciseKey]?.sets || 3,
          reps: prev[exerciseKey]?.reps || 10,
          weight: prev[exerciseKey]?.weight || 0
        }
      }));
    }
  };

  const isExerciseSelected = (exerciseId) => {
    return selectedExercises.some(e => e.key === exerciseId.toString());
  };

  const handleDetailChange = (exerciseId, field, value) => {
    const exerciseKey = exerciseId.toString();
    setExerciseDetails(prev => ({
      ...prev,
      [exerciseKey]: {
        ...prev[exerciseKey],
        [field]: value
      }
    }));
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      const accessToken = localStorage.getItem('accessToken');
      if (!accessToken) {
        setError('No access token found. Please log in again.');
        setSaving(false);
        return;
      }

      const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
      };

      // First, get existing exercises to determine which ones to update vs create
      const existingResponse = await api.get(`/api/sessionExercise/v1/${sessionId}`, { headers });
      const existingExercises = existingResponse.data?._embedded?.sessionExerciseVOList || [];
      
      // Create a map of existing exercise IDs for easier lookup
      const existingExerciseIds = new Set(existingExercises.map(e => e.id.exerciseId));
      
      // Save each exercise with its details
      for (const exercise of selectedExercises) {
        const details = exerciseDetails[exercise.key];
        const data = {
          id: {
            sessionId: parseInt(sessionId),
            exerciseId: parseInt(exercise.key)
          },
          sequence: parseInt(details.sequence),
          sets: parseInt(details.sets),
          reps: parseInt(details.reps),
          weight: parseFloat(details.weight)
        };

        console.log('Saving exercise:', data);
        
        try {
          // If the exercise was already in the session, use PUT
          if (existingExerciseIds.has(parseInt(exercise.key))) {
            await api.put('/api/sessionExercise/v1', data, { headers });
            console.log('Exercise updated successfully');
          } else {
            // Only use POST for new exercises
            const response = await api.post('/api/sessionExercise/v1', data, { headers });
            console.log('Exercise created successfully:', response.data);
          }
        } catch (exerciseError) {
          console.error('Error saving exercise:', exerciseError);
          if (exerciseError.response && exerciseError.response.status === 401) {
            setError('Your session has expired. Please log in again.');
            setSaving(false);
            return;
          }
          // Continue with other exercises even if one fails
        }
      }

      // Handle deletions - remove exercises that are no longer selected
      for (const existingExercise of existingExercises) {
        const isStillSelected = selectedExercises.some(
          e => parseInt(e.key) === existingExercise.id.exerciseId
        );

        if (!isStillSelected) {
          try {
            await api.delete(
              `/api/sessionExercise/v1/${existingExercise.id.sessionId}/${existingExercise.id.exerciseId}`,
              { headers }
            );
            console.log('Exercise deleted successfully');
          } catch (error) {
            console.error('Error deleting exercise:', error);
            // Continue with other operations even if delete fails
          }
        }
      }

      navigate(`/trainingSession/view`);
    } catch (error) {
      console.error('Error saving exercises:', error);
      if (error.response && error.response.status === 401) {
        setError('Your session has expired. Please log in again.');
      } else {
        setError('Failed to save exercises. Please try again.');
      }
    } finally {
      setSaving(false);
    }
  };

  const renderExercises = () => {
    if (loading) {
      return <div className="loading">Loading exercises...</div>;
    }

    if (error) {
      return (
        <div className="error-container">
          <div className="error-message">{error}</div>
          <button className="retry-button" onClick={fetchData}>Retry</button>
        </div>
      );
    }

    return (
      <div className="exercise-groups">
        {groupedExercises.map(group => (
          <div key={group.id} className="exercise-group">
            <div 
              className="group-header" 
              onClick={() => toggleGroup(group.id)}
            >
              <h3>{group.name}</h3>
              <span className="expand-icon">
                {expandedGroups[group.id] ? '−' : '+'}
              </span>
            </div>
            {expandedGroups[group.id] && (
              <div className="group-exercises">
                {group.exercises.map(exercise => (
                  <div 
                    key={exercise.key} 
                    className={`exercise-item ${isExerciseSelected(exercise.key) ? 'selected-exercise' : ''}`}
                    onClick={() => toggleExercise(exercise.key)}
                  >
                    <div className="exercise-name">
                      {exercise.name}
                    </div>
                    {isExerciseSelected(exercise.key) && (
                      <div className="exercise-details">
                        <div className="detail-field">
                          <label>Sequence:</label>
                          <input
                            type="number"
                            value={exerciseDetails[exercise.key]?.sequence || ''}
                            onChange={e => handleDetailChange(exercise.key, 'sequence', e.target.value)}
                            onClick={e => e.stopPropagation()}
                          />
                        </div>
                        <div className="detail-field">
                          <label>Sets:</label>
                          <input
                            type="number"
                            value={exerciseDetails[exercise.key]?.sets || ''}
                            onChange={e => handleDetailChange(exercise.key, 'sets', e.target.value)}
                            onClick={e => e.stopPropagation()}
                          />
                        </div>
                        <div className="detail-field">
                          <label>Reps:</label>
                          <input
                            type="number"
                            value={exerciseDetails[exercise.key]?.reps || ''}
                            onChange={e => handleDetailChange(exercise.key, 'reps', e.target.value)}
                            onClick={e => e.stopPropagation()}
                          />
                        </div>
                        <div className="detail-field">
                          <label>Weight:</label>
                          <input
                            type="number"
                            value={exerciseDetails[exercise.key]?.weight || ''}
                            onChange={e => handleDetailChange(exercise.key, 'weight', e.target.value)}
                            onClick={e => e.stopPropagation()}
                            step="0.1"
                          />
                        </div>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    );
  };

  return (
    <div className="exercises-form">
      <div className="exercises-form-header">
        <h1>Select Exercises</h1>
        <div className="header-buttons">
          <button 
            className="save-button" 
            onClick={handleSave}
            disabled={saving}
          >
            {saving ? 'Saving...' : 'Save Changes'}
          </button>
          <button 
            className="cancel-button" 
            onClick={() => navigate('/trainingSession')}
            disabled={saving}
          >
            Cancel
          </button>
        </div>
      </div>

      {renderExercises()}
    </div>
  );
};

export default ExercisesForm; 