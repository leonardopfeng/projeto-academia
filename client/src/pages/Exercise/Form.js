import React from 'react';
import DynamicForm from '../../components/molecules/DynamicForm';
import api from '../../services/api';
import { useNavigate, useParams } from 'react-router-dom';

const ExerciseForm = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [exerciseGroups, setExerciseGroups] = React.useState([]);
  const [initialData, setInitialData] = React.useState({});

  React.useEffect(() => {
    const fetchExerciseGroups = async () => {
      try {
        const response = await api.get('/api/exerciseGroup/v1', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        const groups = response.data?._embedded?.exerciseGroupVOList || [];
        setExerciseGroups(groups);
      } catch (error) {
        console.error('Error fetching exercise groups:', error);
      }
    };

    const fetchExercise = async () => {
      if (id) {
        try {
          const response = await api.get(`/api/exercise/v1/${id}`, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          });
          console.log('Fetched exercise data:', response.data);
          const exerciseData = {
            key: response.data.key,
            name: response.data.name,
            groupId: response.data.groupId?.toString(),
            videoUrl: response.data.videoUrl || ''
          };
          console.log('Setting initial data:', exerciseData);
          setInitialData(exerciseData);
        } catch (error) {
          console.error('Error fetching exercise:', error);
          navigate('/exercise/view');
        }
      }
    };

    fetchExerciseGroups();
    fetchExercise();
  }, [id, navigate]);

  const fields = [
    {
      name: 'name',
      label: 'Exercise Name',
      type: 'text',
      required: true
    },
    {
      name: 'groupId',
      label: 'Exercise Group',
      type: 'select',
      required: true,
      options: exerciseGroups.map(group => ({
        value: group.key.toString(),
        label: group.name
      }))
    },
    {
      name: 'videoUrl',
      label: 'Video URL',
      type: 'url',
      placeholder: 'https://www.youtube.com/watch?v=...'
    }
  ];

  const handleSubmit = async (formData) => {
    try {
      const headers = {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      };

      const payload = {
        name: formData.name,
        groupId: parseInt(formData.groupId, 10),
        videoUrl: formData.videoUrl || ''
      };

      if (id) {
        // Update existing exercise - include key in payload
        payload.key = parseInt(id, 10);
        await api.put(`/api/exercise/v1`, payload, { headers });
      } else {
        // Create new exercise
        await api.post('/api/exercise/v1', payload, { headers });
      }
      navigate('/exercise/view');
    } catch (error) {
      console.error('Error saving exercise:', error);
      alert('Failed to save exercise');
    }
  };

  return (
    <DynamicForm
      fields={fields}
      onSubmit={handleSubmit}
      initialData={initialData}
      title={id ? 'Edit Exercise' : 'Create Exercise'}
      submitButtonText={id ? 'Update' : 'Create'}
    />
  );
};

export default ExerciseForm; 