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
            startDate: sessionResponse.data.startDate.split('T')[0],
            clientId: sessionResponse.data.clientId.toString(),
            coachId: sessionResponse.data.coachId.toString(),
            status: sessionResponse.data.status
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
      type: 'date',
      required: true
    },
    {
      name: 'status',
      label: 'Status (Active)',
      type: 'checkbox',
      required: false
    }
  ];

  const handleSubmit = async (data) => {
    try {
      const headers = {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      };

      // Detailed debugging for checkbox
      console.log('--- DETAILED DEBUGGING ---');
      console.log('Raw form data:', data);
      console.log('Status field type:', typeof data.status);
      console.log('Status field value:', data.status);
      console.log('Status field truthiness:', Boolean(data.status));
      console.log('-------------------------');

      // Format the data with boolean status
      const formattedData = {
        ...data,
        startDate: data.startDate + 'T00:00:00',
        // Ensure status is a boolean, will be true if checked, false if not
        status: Boolean(data.status)
      };

      // For PUT requests, ensure the key is included
      if (id) {
        formattedData.key = parseInt(id);
      }

      console.log('Final formatted data:', JSON.stringify(formattedData, null, 2));

      let response;
      if (id) {
        response = await api.put('/api/trainingSession/v1', formattedData, { headers });
      } else {
        response = await api.post('/api/trainingSession/v1', formattedData, { headers });
      }

      const sessionKey = response.data.key;
      navigate(`/trainingSession/${sessionKey}/exercises`);
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
      <div className="form-header">
        <h2>{id ? 'Edit Training Session' : 'Create Training Session'}</h2>
        {id && (
          <button
            className="exercises-button"
            onClick={() => navigate(`/trainingSession/${id}/exercises`)}
          >
            Exercises
          </button>
        )}
      </div>
      <DynamicForm
        fields={fields}
        onSubmit={handleSubmit}
        initialData={initialData}
        submitButtonText={id ? 'Update' : 'Create'}
      />
    </div>
  );
};

export default TrainingSessionForm; 