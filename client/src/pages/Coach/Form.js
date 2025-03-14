import React from 'react';
import DynamicForm from '../../components/molecules/DynamicForm';
import api from '../../services/api';
import { useNavigate, useParams } from 'react-router-dom';

const CoachForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [initialData, setInitialData] = React.useState({});
  const [users, setUsers] = React.useState([]);

  React.useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await api.get('/api/user/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const usersData = response.data?._embedded?.userVOList || [];
        setUsers(usersData);
      } catch (error) {
        console.error('Error fetching users:', error);
        setUsers([]);
      }
    };

    const fetchCoach = async () => {
      if (id) {
        try {
          const response = await api.get(`/api/coach/v1/${id}`, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          });
          const coachData = {
            key: response.data.key,
            userId: response.data.userId,
            certification: response.data.certification,
            hiredDate: response.data.hiredDate
          };
          setInitialData(coachData);
        } catch (error) {
          console.error('Error fetching coach:', error);
          navigate('/coach/view');
        }
      }
    };

    fetchUsers();
    fetchCoach();
  }, [id, navigate]);

  const fields = [
    {
      name: 'userId',
      label: 'User',
      type: 'select',
      required: true,
      options: users.map(user => ({
        value: user.key,
        label: `${user.fullName} (${user.userName})`
      }))
    },
    {
      name: 'certification',
      label: 'Certification',
      type: 'text',
      required: true
    },
    {
      name: 'hiredDate',
      label: 'Hired Date',
      type: 'date',
      required: true
    }
  ];

  const handleSubmit = async (formData) => {
    try {
      const headers = {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      };

      const payload = {
        key: parseInt(formData.userId, 10), // Use the selected user's key
        certification: formData.certification,
        hiredDate: formData.hiredDate
      };

      if (id) {
        // Update existing coach
        payload.key = parseInt(id, 10);
        await api.put(`/api/coach/v1`, payload, { headers });
      } else {
        // Create new coach
        await api.post('/api/coach/v1', payload, { headers });
      }
      navigate('/coach/view');
    } catch (error) {
      console.error('Error saving coach:', error);
      alert('Failed to save coach');
    }
  };

  return (
    <DynamicForm
      fields={fields}
      onSubmit={handleSubmit}
      initialData={initialData}
      title={id ? 'Edit Coach' : 'Create Coach'}
      submitButtonText={id ? 'Update' : 'Create'}
    />
  );
};

export default CoachForm; 