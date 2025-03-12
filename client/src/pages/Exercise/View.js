import React from 'react';
import { useNavigate } from 'react-router-dom';
import DataView from '../../components/molecules/DataView';
import api from "../../services/api";
import { mockExercises } from '../../services/mockData';
import './View.css';

const ExerciseView = () => {
  const navigate = useNavigate();

  const handleExerciseClick = (exercise) => {
    navigate(`/exercise/${exercise.key}`);
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
  
  const [exercises, setExercises] = React.useState([]);

  React.useEffect(() => {
    const fetchExercises = async () => {
      try {
        const response = await api.get('/api/exercise/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        
        // Access the nested exerciseVOList
        const exercisesData = response.data?._embedded?.exerciseVOList || [];
        setExercises(exercisesData);
      } catch (error) {
        console.error('Error fetching exercises:', error);
        setExercises([]);
      }
    };

    fetchExercises();
  }, []);

  return (
    <div className="exercise-view">
      <h1>Exercises</h1>
      <DataView
        data={exercises}
        fields={['name', 'groupId', 'videoUrl']}
        onItemClick={handleExerciseClick}
        onDelete={handleDelete}
        showVideoButton={true}
      />
    </div>
  );
};

export default ExerciseView; 