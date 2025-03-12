import React from 'react';
import { useNavigate } from 'react-router-dom';
import DataView from '../../components/molecules/DataView';
import api from "../../services/api";
import './View.css';

const ExerciseGroupView = () => {
  const navigate = useNavigate();
  const [exerciseGroups, setExerciseGroups] = React.useState([]);

  const handleExerciseGroupClick = (exerciseGroup) => {
    navigate(`/exercise-group/${exerciseGroup.id}`);
  };

  const handleDelete = async (exerciseGroup) => {
    if (window.confirm('Are you sure you want to delete this exercise group?')) {
      try {
        await api.delete(`/api/exerciseGroup/v1/${exerciseGroup.key}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        
        // Remove the deleted group from the state
        setExerciseGroups(prevGroups => 
          prevGroups.filter(g => g.key !== exerciseGroup.key)
        );
      } catch (error) {
        console.error('Error deleting exercise group:', error);
        alert('Failed to delete exercise group');
      }
    }
  };

  React.useEffect(() => {
    const fetchExerciseGroups = async () => {
      try {
        const response = await api.get('/api/exerciseGroup/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        
        // Access the nested exerciseGroupVOList (assuming similar structure to exercise endpoint)
        const groupsData = response.data?._embedded?.exerciseGroupVOList || [];
        setExerciseGroups(groupsData);
      } catch (error) {
        console.error('Error fetching exercise groups:', error);
        setExerciseGroups([]);
      }
    };

    fetchExerciseGroups();
  }, []);

  return (
    <div className="exercise-group-view">
      <h1>Exercise Groups</h1>
      <DataView
        data={exerciseGroups}
        fields={['name']}
        onItemClick={handleExerciseGroupClick}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default ExerciseGroupView; 