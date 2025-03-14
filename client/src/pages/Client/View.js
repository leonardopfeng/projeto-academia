import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DataView from '../../components/molecules/DataView';
import api from '../../services/api';
import './View.css';

const ClientView = () => {
  const navigate = useNavigate();
  const [clients, setClients] = useState([]);
  const [users, setUsers] = useState({});
  const [coaches, setCoaches] = useState({});
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch clients
        const clientsResponse = await api.get('/api/client/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const clientsData = clientsResponse.data?._embedded?.clientVOList || [];
        setClients(clientsData);

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

        // Fetch all coaches
        const coachesResponse = await api.get('/api/coach/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const coachesData = coachesResponse.data?._embedded?.coachVOList || [];
        
        // Create a map of coach IDs to coach data with user names
        const coachesMap = {};
        coachesData.forEach(coach => {
          coachesMap[coach.key] = {
            ...coach,
            fullName: usersMap[coach.userId]?.userName || 'Unknown Coach'
          };
        });
        setCoaches(coachesMap);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleDelete = async (client) => {
    if (window.confirm('Are you sure you want to delete this client?')) {
      try {
        await api.delete(`/api/client/v1/${client.key}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        setClients(clients.filter(c => c.key !== client.key));
      } catch (error) {
        console.error('Error deleting client:', error);
        alert('Failed to delete client');
      }
    }
  };

  const handleEdit = (client) => {
    navigate(`/client/edit/${client.key}`);
  };

  const filteredClients = clients.filter(client => {
    const searchLower = searchTerm.toLowerCase();
    const clientUser = users[client.userId];
    const clientCoach = coaches[client.coachId];
    return (
      clientUser?.userName?.toLowerCase().includes(searchLower) ||
      clientCoach?.userName?.toLowerCase().includes(searchLower)
    );
  });

  const displayFields = ['clientName', 'coachName'];

  const processedClients = filteredClients.map(client => ({
    ...client,
    clientName: users[client.userId]?.userName || 'Unknown User',
    coachName: coaches[client.coachId]?.userName || 'Unknown Coach'
  }));

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="client-view">
      <div className="client-view-header">
        <h1>Clients</h1>
        <button 
          className="add-button"
          onClick={() => navigate('/client/add')}
        >
          Add Client
        </button>
      </div>

      <div className="search-container">
        <input
          type="text"
          placeholder="Search by client or coach name..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
      </div>

      <DataView
        data={processedClients}
        fields={displayFields}
        onItemClick={handleEdit}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default ClientView; 