import React from 'react';
import DynamicForm from '../../components/molecules/DynamicForm';
import api from '../../services/api';
import { useNavigate, useParams } from 'react-router-dom';

const ExerciseGroupForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [initialData, setInitialData] = React.useState({});

  React.useEffect(() => {
    const fetchExerciseGroup = async () => {
      if (id) {
        try {
          const response = await api.get(`/api/exerciseGroup/v1/${id}`, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          });
          console.log('Fetched group data:', response.data);
          const groupData = {
            key: response.data.key,
            name: response.data.name || ''
          };
          console.log('Setting initial data:', groupData);
          setInitialData(groupData);
        } catch (error) {
          console.error('Error fetching exercise group:', error);
          navigate('/exerciseGroup/view');
        }
      }
    };

    fetchExerciseGroup();
  }, [id, navigate]);

  const fields = [
    {
      name: 'name',
      label: 'Group Name',
      type: 'text',
      required: true
    }
  ];

  const handleSubmit = async (formData) => {
    try {
      const headers = {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      };

      const payload = {
        name: formData.name
      };

      if (id) {
        // Update existing exercise group - include key in payload
        payload.key = parseInt(id, 10);
        await api.put(`/api/exerciseGroup/v1`, payload, { headers });
      } else {
        // Create new exercise group
        await api.post('/api/exerciseGroup/v1', payload, { headers });
      }
      navigate('/exerciseGroup/view');
    } catch (error) {
      console.error('Error saving exercise group:', error);
      alert('Failed to save exercise group');
    }
  };

  return (
    <DynamicForm
      fields={fields}
      onSubmit={handleSubmit}
      initialData={initialData}
      title={id ? 'Edit Exercise Group' : 'Create Exercise Group'}
      submitButtonText={id ? 'Update' : 'Create'}
    />
  );
};

export default ExerciseGroupForm; 