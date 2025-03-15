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

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('accessToken');
        if (!token) {
          navigate('/');
          return;
        }

        const headers = {
          'Authorization': `Bearer ${token}`
        };

        // First fetch exercise groups
        const groupsResponse = await api.get('/api/exerciseGroup/v1', { headers });
        
        const groups = groupsResponse.data?._embedded?.exerciseGroupVOList || [];
        const groupMap = {};
        groups.forEach(group => {
          groupMap[group.key] = group.name;
        });
        setGroupNames(groupMap);

        // Then fetch exercises
        const exercisesResponse = await api.get('/api/exercise/v1', { headers });
        
        // Group exercises by muscle group
        const exercisesByGroup = {};
        const exercisesList = exercisesResponse.data?._embedded?.exerciseVOList || [];
        
        exercisesList.forEach(exercise => {
          const groupName = groupMap[exercise.groupId] || 'Other';
          if (!exercisesByGroup[groupName]) {
            exercisesByGroup[groupName] = [];
          }
          exercisesByGroup[groupName].push(exercise);
        });

        setExercises(exercisesByGroup);
        
        // Initialize expanded state for all groups
        const initialExpandedState = {};
        Object.keys(exercisesByGroup).forEach(group => {
          initialExpandedState[group] = true;
        });
        setExpandedGroups(initialExpandedState);

        // Fetch existing exercises for this session
        try {
          const sessionExercisesResponse = await api.get(`/api/sessionExercise/v1/${sessionId}`, { headers });
          const sessionExercises = sessionExercisesResponse.data?._embedded?.sessionExerciseVOList || [];
          
          // Find the corresponding exercises and set them as selected
          const selectedExs = [];
          const details = {};
          
          sessionExercises.forEach(sessionExercise => {
            const exercise = exercisesList.find(e => e.key === sessionExercise.id.exerciseId);
            if (exercise) {
              selectedExs.push(exercise);
              details[exercise.key] = {
                sequence: sessionExercise.sequence,
                sets: sessionExercise.sets,
                reps: sessionExercise.reps,
                weight: sessionExercise.weight
              };
            }
          });

          setSelectedExercises(selectedExs);
          setExerciseDetails(details);
        } catch (error) {
          console.error('Error fetching session exercises:', error);
          // Don't set error state here as we still want to show the form
          // Just log the error and continue
        }

        setError(null);
      } catch (error) {
        console.error('Error fetching data:', error);
        if (error.response?.status === 401 || error.response?.status === 403) {
          localStorage.removeItem('accessToken');
          navigate('/');
        } else {
          setError('Failed to load exercises. Please try again later.');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [navigate, sessionId]);

  const toggleGroup = (group) => {
    setExpandedGroups(prev => ({
      ...prev,
      [group]: !prev[group]
    }));
  };

  const toggleExercise = (exercise) => {
    setSelectedExercises(prev => {
      const isSelected = prev.some(e => e.key === exercise.key);
      if (isSelected) {
        // Remove exercise details when unselecting
        const newDetails = { ...exerciseDetails };
        delete newDetails[exercise.key];
        setExerciseDetails(newDetails);
        return prev.filter(e => e.key !== exercise.key);
      } else {
        // Initialize exercise details when selecting
        setExerciseDetails(prev => ({
          ...prev,
          [exercise.key]: {
            sequence: selectedExercises.length + 1,
            sets: 3,
            reps: 10,
            weight: 0
          }
        }));
        return [...prev, exercise];
      }
    });
  };

  const handleDetailChange = (exerciseKey, field, value) => {
    setExerciseDetails(prev => ({
      ...prev,
      [exerciseKey]: {
        ...prev[exerciseKey],
        [field]: value
      }
    }));
  };

  const handleSave = async () => {
    try {
      setSaving(true);
      const token = localStorage.getItem('accessToken');
      
      if (!token) {
        console.error('No access token found');
        navigate('/');
        return;
      }

      const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      };

      console.log('Starting to save exercises...');
      
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
          console.error('Error response:', exerciseError.response);
          if (exerciseError.response?.status === 401) {
            localStorage.removeItem('accessToken');
            navigate('/');
            return;
          }
          throw exerciseError;
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

      console.log('All exercises saved successfully');
      navigate('/trainingSession/view');
    } catch (error) {
      console.error('Error in handleSave:', error);
      console.error('Error details:', {
        status: error.response?.status,
        data: error.response?.data,
        headers: error.response?.headers
      });

      if (error.response?.status === 401 || error.response?.status === 403) {
        console.error('Authentication error - redirecting to login');
        localStorage.removeItem('accessToken');
        navigate('/');
      } else {
        alert(`Failed to save exercises: ${error.response?.data?.message || 'Unknown error'}`);
      }
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  if (error) {
    return (
      <div className="error-container">
        <p className="error-message">{error}</p>
        <button 
          className="retry-button"
          onClick={() => window.location.reload()}
        >
          Retry
        </button>
      </div>
    );
  }

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

      <div className="exercise-groups">
        {Object.entries(exercises).map(([group, groupExercises]) => (
          <div key={group} className="exercise-group">
            <div 
              className="group-header" 
              onClick={() => toggleGroup(group)}
            >
              <h2>{group}</h2>
              <span className={`arrow ${expandedGroups[group] ? 'expanded' : ''}`}>
                ▼
              </span>
            </div>
            {expandedGroups[group] && (
              <div className="exercise-list">
                {groupExercises.map(exercise => {
                  const isSelected = selectedExercises.some(
                    e => e.key === exercise.key
                  );
                  return (
                    <div
                      key={exercise.key}
                      className={`exercise-item ${isSelected ? 'selected' : ''}`}
                    >
                      <div className="exercise-item-header" onClick={() => toggleExercise(exercise)}>
                        <span className="exercise-name">{exercise.name}</span>
                        {isSelected && <span className="check-mark">✓</span>}
                      </div>
                      {isSelected && (
                        <div className="exercise-details">
                          <div className="detail-field">
                            <label>Sequence:</label>
                            <input
                              type="number"
                              min="1"
                              value={exerciseDetails[exercise.key]?.sequence || 1}
                              onChange={(e) => handleDetailChange(exercise.key, 'sequence', e.target.value)}
                            />
                          </div>
                          <div className="detail-field">
                            <label>Sets:</label>
                            <input
                              type="number"
                              min="1"
                              value={exerciseDetails[exercise.key]?.sets || 3}
                              onChange={(e) => handleDetailChange(exercise.key, 'sets', e.target.value)}
                            />
                          </div>
                          <div className="detail-field">
                            <label>Reps:</label>
                            <input
                              type="number"
                              min="1"
                              value={exerciseDetails[exercise.key]?.reps || 10}
                              onChange={(e) => handleDetailChange(exercise.key, 'reps', e.target.value)}
                            />
                          </div>
                          <div className="detail-field">
                            <label>Weight (kg):</label>
                            <input
                              type="number"
                              min="0"
                              step="0.5"
                              value={exerciseDetails[exercise.key]?.weight || 0}
                              onChange={(e) => handleDetailChange(exercise.key, 'weight', e.target.value)}
                            />
                          </div>
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ExercisesForm; 