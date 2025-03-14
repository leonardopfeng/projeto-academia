import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import DataView from '../../components/molecules/DataView';
import api from '../../services/api';
import './View.css';

const TrainingSessionView = () => {
  const navigate = useNavigate();
  const [sessions, setSessions] = useState([]);
  const [users, setUsers] = useState({});
  const [clients, setClients] = useState({});
  const [coaches, setCoaches] = useState({});
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch users first as we need them for both clients and coaches
        const usersResponse = await api.get('/api/user/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const usersData = usersResponse.data?._embedded?.userVOList || [];
        const usersMap = {};
        usersData.forEach(user => {
          usersMap[user.key] = user;
        });
        setUsers(usersMap);

        // Fetch clients
        const clientsResponse = await api.get('/api/client/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const clientsData = clientsResponse.data?._embedded?.clientVOList || [];
        const clientsMap = {};
        clientsData.forEach(client => {
          clientsMap[client.userId] = {
            ...client,
            userName: usersMap[client.userId]?.userName || 'Unknown User'
          };
        });
        setClients(clientsMap);

        // Fetch coaches
        const coachesResponse = await api.get('/api/coach/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const coachesData = coachesResponse.data?._embedded?.coachVOList || [];
        const coachesMap = {};
        coachesData.forEach(coach => {
          coachesMap[coach.key] = {
            ...coach,
            userName: usersMap[coach.key]?.userName || 'Unknown Coach'
          };
        });
        setCoaches(coachesMap);

        // Fetch training sessions
        const sessionsResponse = await api.get('/api/trainingSession/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const sessionsData = sessionsResponse.data?._embedded?.trainingSessionVOList || [];
        setSessions(sessionsData);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleDelete = async (session) => {
    if (window.confirm('Are you sure you want to delete this training session?')) {
      try {
        await api.delete(`/api/trainingSession/v1/${session.key}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        setSessions(sessions.filter(s => s.key !== session.key));
      } catch (error) {
        console.error('Error deleting training session:', error);
        alert('Failed to delete training session');
      }
    }
  };

  const handleEdit = (session) => {
    navigate(`/trainingSession/edit/${session.key}`);
  };

  const filteredSessions = sessions.filter(session => {
    const clientName = clients[session.clientId]?.userName || '';
    const coachName = coaches[session.coachId]?.userName || '';
    const searchLower = searchTerm.toLowerCase();
    
    return (
      clientName.toLowerCase().includes(searchLower) ||
      coachName.toLowerCase().includes(searchLower) ||
      session.name.toLowerCase().includes(searchLower)
    );
  });

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  const renderStatus = (status) => {
    const style = {
      backgroundColor: status ? '#e6ffe6' : '#ffe6e6',
      color: status ? '#006600' : '#cc0000',
      padding: '4px 8px',
      borderRadius: '4px',
      fontWeight: 'bold',
      display: 'inline-block'
    };
    
    return <span style={style}>{status ? 'Active' : 'Inactive'}</span>;
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  const fields = ['Client', 'Coach', 'Name', 'Start Date', 'Status'];

  const processedSessions = filteredSessions.map(session => ({
    key: session.key,
    'Client': clients[session.clientId]?.userName || 'Unknown Client',
    'Coach': coaches[session.coachId]?.userName || 'Unknown Coach',
    'Name': session.name,
    'Start Date': formatDate(session.startDate),
    'Status': renderStatus(session.status)
  }));

  return (
    <div className="training-session-view">
      <div className="training-session-view-header">
        <h1>Training Sessions</h1>
        <button 
          className="add-button"
          onClick={() => navigate('/trainingSession/add')}
        >
          Add Training Session
        </button>
      </div>

      <div className="search-container">
        <input
          type="text"
          placeholder="Search training sessions..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
      </div>

      <DataView
        data={processedSessions}
        fields={fields}
        onItemClick={handleEdit}
        onDelete={handleDelete}
        idField="key"
      />
    </div>
  );
};

export default TrainingSessionView; 