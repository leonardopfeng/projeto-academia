import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import DynamicForm from '../../components/molecules/DynamicForm';
import api from '../../services/api';
import './Form.css';

const ClientForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [users, setUsers] = useState([]);
  const [coaches, setCoaches] = useState([]);
  const [clientData, setClientData] = useState({
    userId: '',
    coachId: ''
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch users
        const usersResponse = await api.get('/api/user/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const usersData = usersResponse.data?._embedded?.userVOList || [];
        setUsers(usersData);

        // Fetch coaches
        const coachesResponse = await api.get('/api/coach/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const coachesData = coachesResponse.data?._embedded?.coachVOList || [];
        
        // Get coach users data to display names
        const coachUsersResponse = await api.get('/api/user/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const coachUsersData = coachUsersResponse.data?._embedded?.userVOList || [];
        const coachUsersMap = {};
        coachUsersData.forEach(user => {
          coachUsersMap[user.key] = user;
        });

        // Add user names to coaches data
        const coachesWithNames = coachesData.map(coach => ({
          ...coach,
          fullname: coachUsersMap[coach.userId]?.fullname || 'Unknown Coach'
        }));
        setCoaches(coachesWithNames);

        if (id) {
          const clientResponse = await api.get(`/api/client/v1/${id}`, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          });
          setClientData(clientResponse.data);
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const handleSubmit = async (formData) => {
    try {
      const payload = {
        ...formData,
        userId: parseInt(formData.userId),
        coachId: parseInt(formData.coachId)
      };

      if (id) {
        await api.put(`/api/client/v1/${id}`, payload, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
      } else {
        await api.post('/api/client/v1', payload, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
      }

      navigate('/client/view');
    } catch (error) {
      console.error('Error saving client:', error);
      alert('Failed to save client');
    }
  };

  const fields = [
    {
      name: 'userId',
      label: 'User',
      type: 'select',
      required: true,
      options: users.map(user => ({
        value: user.key,
        label: `${user.fullname} (${user.userName})`
      }))
    },
    {
      name: 'coachId',
      label: 'Coach',
      type: 'select',
      required: true,
      options: coaches.map(coach => ({
        value: coach.key,
        label: coach.fullname
      }))
    }
  ];

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="client-form">
      <h1>{id ? 'Edit Client' : 'Add Client'}</h1>
      <DynamicForm
        fields={fields}
        initialData={clientData}
        onSubmit={handleSubmit}
      />
    </div>
  );
};

export default ClientForm; 