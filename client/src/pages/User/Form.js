import React from 'react';
import DynamicForm from '../../components/molecules/DynamicForm';
import api from '../../services/api';
import { useNavigate, useParams } from 'react-router-dom';

const UserForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [initialData, setInitialData] = React.useState({});

  React.useEffect(() => {
    const fetchUser = async () => {
      if (id) {
        try {
          const response = await api.get(`/api/user/v1/${id}`, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          });
          console.log('Fetched user data:', response.data);
          const userData = {
            key: response.data.key,
            userName: response.data.userName,
            fullname: response.data.fullname,
            email: response.data.email,
            phone: response.data.phone,
            roles: response.data.roles || []
          };
          console.log('Setting initial data:', userData);
          setInitialData(userData);
        } catch (error) {
          console.error('Error fetching user:', error);
          navigate('/user/view');
        }
      }
    };

    fetchUser();
  }, [id, navigate]);

  const fields = [
    {
      name: 'userName',
      label: 'Username',
      type: 'text',
      required: true
    },
    {
      name: 'password',
      label: 'Password',
      type: 'password',
      required: !id // Password is required only for new users
    },
    {
      name: 'fullname',
      label: 'Full Name',
      type: 'text',
      required: true
    },
    {
      name: 'email',
      label: 'Email',
      type: 'email',
      required: true
    },
    {
      name: 'phone',
      label: 'Phone',
      type: 'tel',
      required: true
    },
    {
      name: 'roles',
      label: 'Roles',
      type: 'multiselect',
      required: true,
      options: [
        { value: 'ADMIN', label: 'Administrator' },
        { value: 'MANAGER', label: 'Manager' },
        { value: 'COMMON_USER', label: 'Common User' }
      ]
    }
  ];

  const handleSubmit = async (formData) => {
    try {
      const headers = {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      };

      const payload = {
        userName: formData.userName,
        fullname: formData.fullname,
        email: formData.email,
        phone: formData.phone,
        roles: formData.roles || []
      };

      // Only include password in payload if it's provided (new user or password change)
      if (formData.password) {
        payload.password = formData.password;
      }

      if (id) {
        // Update existing user
        payload.key = parseInt(id, 10);
        await api.put(`/api/user/v1`, payload, { headers });
      } else {
        // Create new user
        await api.post('/api/user/v1', payload, { headers });
      }
      navigate('/user/view');
    } catch (error) {
      console.error('Error saving user:', error);
      alert('Failed to save user');
    }
  };

  return (
    <DynamicForm
      fields={fields}
      onSubmit={handleSubmit}
      initialData={initialData}
      title={id ? 'Edit User' : 'Create User'}
      submitButtonText={id ? 'Update' : 'Create'}
    />
  );
};

export default UserForm; 