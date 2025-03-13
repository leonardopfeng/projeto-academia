import React from 'react';
import { useNavigate } from 'react-router-dom';
import DataView from '../../components/molecules/DataView';
import SearchFilter from '../../components/molecules/SearchFilter';
import api from "../../services/api";
import './View.css';

const ExerciseGroupView = () => {
  const navigate = useNavigate();
  const [exerciseGroups, setExerciseGroups] = React.useState([]);
  const [filteredGroups, setFilteredGroups] = React.useState([]);

  React.useEffect(() => {
    const fetchExerciseGroups = async () => {
      try {
        const response = await api.get('/api/exerciseGroup/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        
        // Access the nested exerciseGroupVOList
        const groupsData = response.data?._embedded?.exerciseGroupVOList || [];
        setExerciseGroups(groupsData);
      } catch (error) {
        console.error('Error fetching exercise groups:', error);
        setExerciseGroups([]);
      }
    };

    fetchExerciseGroups();
  }, []);

  React.useEffect(() => {
    setFilteredGroups(exerciseGroups);
  }, [exerciseGroups]);

  const handleExerciseGroupClick = (exerciseGroup) => {
    navigate(`/exerciseGroup/edit/${exerciseGroup.key}`);
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

  const searchFields = [
    { key: 'name', label: 'Group Name' }
  ];

  const handleFilterChange = (searchValues) => {
    const filtered = exerciseGroups.filter(group => {
      return Object.entries(searchValues).every(([key, value]) => {
        if (!value) return true; // if search value is empty, include all
        const fieldValue = group[key]?.toString().toLowerCase() || '';
        return fieldValue.includes(value.toLowerCase());
      });
    });
    setFilteredGroups(filtered);
  };

  return (
    <div className="exercise-group-view">
      <div className="exercise-group-view-header">
        <h1>Exercise Groups</h1>
        <button 
          className="add-button"
          onClick={() => navigate('/exerciseGroup/add')}
        >
          Add Group
        </button>
      </div>
      <SearchFilter 
        searchFields={searchFields}
        onFilterChange={handleFilterChange}
      />
      <DataView
        data={filteredGroups}
        fields={['name']}
        onItemClick={handleExerciseGroupClick}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default ExerciseGroupView; 