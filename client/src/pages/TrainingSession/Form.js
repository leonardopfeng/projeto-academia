import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import DynamicForm from '../../components/molecules/DynamicForm';
import api from '../../services/api';
import './Form.css';

const TrainingSessionForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [clients, setClients] = useState([]);
  const [coaches, setCoaches] = useState([]);
  const [users, setUsers] = useState({});
  const [initialData, setInitialData] = useState({});
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
        setClients(clientsData);

        // Fetch coaches
        const coachesResponse = await api.get('/api/coach/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const coachesData = coachesResponse.data?._embedded?.coachVOList || [];
        setCoaches(coachesData);

        // If editing, fetch existing training session
        if (id) {
          const sessionResponse = await api.get(`/api/trainingSession/v1/${id}`, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          });
          setInitialData({
            ...sessionResponse.data,
            startDate: sessionResponse.data.startDate.split('T')[0], // Format date for input
            clientId: sessionResponse.data.clientId.toString(),
            coachId: sessionResponse.data.coachId.toString()
          });
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const fields = [
    {
      name: 'clientId',
      label: 'Client',
      type: 'select',
      required: true,
      options: clients.map(client => ({
        value: client.userId.toString(),
        label: users[client.userId]?.userName || 'Unknown User'
      }))
    },
    {
      name: 'coachId',
      label: 'Coach',
      type: 'select',
      required: true,
      options: coaches.map(coach => ({
        value: coach.key.toString(),
        label: `${users[coach.key]?.userName || 'Unknown Coach'} - ${coach.certification}`
      }))
    },
    {
      name: 'name',
      label: 'Session Name',
      type: 'text',
      required: true
    },
    {
      name: 'startDate',
      label: 'Start Date',
      type: 'datetime-local',
      required: true
    },
    {
      name: 'status',
      label: 'Status',
      type: 'checkbox',
      required: false
    }
  ];

  const handleSubmit = async (formData) => {
    try {
      const payload = {
        clientId: parseInt(formData.clientId),
        coachId: parseInt(formData.coachId),
        name: formData.name,
        startDate: new Date(formData.startDate).toISOString(),
        status: formData.status || false
      };

      const headers = {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      };

      if (id) {
        payload.key = parseInt(id);
        await api.put(`/api/trainingSession/v1`, payload, { headers });
      } else {
        await api.post('/api/trainingSession/v1', payload, { headers });
      }

      navigate('/trainingSession/view');
    } catch (error) {
      console.error('Error saving training session:', error);
      alert('Failed to save training session');
    }
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="form-container">
      <DynamicForm
        fields={fields}
        onSubmit={handleSubmit}
        initialData={initialData}
        title={id ? 'Edit Training Session' : 'Create Training Session'}
        submitButtonText={id ? 'Update' : 'Create'}
      />
    </div>
  );
};

export default TrainingSessionForm; 