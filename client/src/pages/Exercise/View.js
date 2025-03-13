import React from 'react';
import { useNavigate } from 'react-router-dom';
import DataView from '../../components/molecules/DataView';
import SearchFilter from '../../components/molecules/SearchFilter';
import api from "../../services/api";
import './View.css';

const ExerciseView = () => {
  const navigate = useNavigate();
  const [exercises, setExercises] = React.useState([]);
  const [filteredExercises, setFilteredExercises] = React.useState([]);
  const [groupNames, setGroupNames] = React.useState({});

  React.useEffect(() => {
    const fetchExerciseGroups = async () => {
      try {
        const response = await api.get('/api/exerciseGroup/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        
        const groups = response.data?._embedded?.exerciseGroupVOList || [];
        const groupMap = {};
        groups.forEach(group => {
          groupMap[group.key] = group.name;
        });
        setGroupNames(groupMap);
      } catch (error) {
        console.error('Error fetching exercise groups:', error);
      }
    };

    fetchExerciseGroups();
  }, []);

  React.useEffect(() => {
    const fetchExercises = async () => {
      try {
        const response = await api.get('/api/exercise/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        
        // Access the nested exerciseVOList and transform the data
        const exercisesData = response.data?._embedded?.exerciseVOList || [];
        const transformedExercises = exercisesData.map(exercise => ({
          ...exercise,
          groupName: groupNames[exercise.groupId] || 'Unknown Group'
        }));
        setExercises(transformedExercises);
      } catch (error) {
        console.error('Error fetching exercises:', error);
        setExercises([]);
      }
    };

    if (Object.keys(groupNames).length > 0) {
      fetchExercises();
    }
  }, [groupNames]);

  React.useEffect(() => {
    setFilteredExercises(exercises);
  }, [exercises]);

  const handleExerciseClick = (exercise) => {
    navigate(`/exercise/edit/${exercise.key}`);
  };

  const handleDelete = async (exercise) => {
    if (window.confirm('Are you sure you want to delete this exercise?')) {
      try {
        await api.delete(`/api/exercise/v1/${exercise.key}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        
        // Remove the deleted exercise from the state
        setExercises(prevExercises => 
          prevExercises.filter(e => e.key !== exercise.key)
        );
      } catch (error) {
        console.error('Error deleting exercise:', error);
        alert('Failed to delete exercise');
      }
    }
  };

  const searchFields = [
    { key: 'name', label: 'Exercise Name' },
    { key: 'groupName', label: 'Group Name' }
  ];

  const handleFilterChange = (searchValues) => {
    const filtered = exercises.filter(exercise => {
      return Object.entries(searchValues).every(([key, value]) => {
        if (!value) return true; // if search value is empty, include all
        const fieldValue = exercise[key]?.toString().toLowerCase() || '';
        return fieldValue.includes(value.toLowerCase());
      });
    });
    setFilteredExercises(filtered);
  };

  return (
    <div className="exercise-view">
      <div className="exercise-view-header">
        <h1>Exercises</h1>
        <button 
          className="add-button"
          onClick={() => navigate('/exercise/add')}
        >
          Add Exercise
        </button>
      </div>
      <SearchFilter 
        searchFields={searchFields}
        onFilterChange={handleFilterChange}
      />
      <DataView
        data={filteredExercises}
        fields={['name', 'groupName']}
        onItemClick={handleExerciseClick}
        onDelete={handleDelete}
        showVideoButton={true}
      />
    </div>
  );
};

export default ExerciseView; 