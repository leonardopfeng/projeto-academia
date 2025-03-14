import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DataView from '../../components/molecules/DataView';
import api from '../../services/api';
import './View.css';

const CoachView = () => {
  const navigate = useNavigate();
  const [coaches, setCoaches] = useState([]);
  const [users, setUsers] = useState({});
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch coaches
        const coachesResponse = await api.get('/api/coach/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const coachesData = coachesResponse.data?._embedded?.coachVOList || [];
        setCoaches(coachesData);

        // Fetch all users
        const usersResponse = await api.get('/api/user/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const usersData = usersResponse.data?._embedded?.userVOList || [];
        
        // Create a map of user IDs to user data
        const usersMap = {};
        usersData.forEach(user => {
          usersMap[user.key] = user;
        });
        setUsers(usersMap);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleDelete = async (coach) => {
    if (window.confirm('Are you sure you want to delete this coach?')) {
      try {
        await api.delete(`/api/coach/v1/${coach.key}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        setCoaches(coaches.filter(c => c.key !== coach.key));
      } catch (error) {
        console.error('Error deleting coach:', error);
        alert('Failed to delete coach');
      }
    }
  };

  const handleEdit = (coach) => {
    navigate(`/coach/edit/${coach.key}`);
  };

  const filteredCoaches = coaches.filter(coach => {
    const searchLower = searchTerm.toLowerCase();
    const coachUser = users[coach.userId];
    return (
      coachUser?.userName?.toLowerCase().includes(searchLower) ||
      coach.certification?.toLowerCase().includes(searchLower)
    );
  });

  const displayFields = ['name', 'certification', 'hiredDate'];

  const processedCoaches = filteredCoaches.map(coach => ({
    ...coach,
    name: users[coach.userId]?.userName || 'Unknown User'
  }));

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="coach-view">
      <div className="coach-view-header">
        <h1>Coaches</h1>
        <button 
          className="add-button"
          onClick={() => navigate('/coach/add')}
        >
          Add Coach
        </button>
      </div>

      <div className="search-container">
        <input
          type="text"
          placeholder="Search by name, username or certification..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
      </div>

      <DataView
        data={processedCoaches}
        fields={displayFields}
        onItemClick={handleEdit}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default CoachView; 