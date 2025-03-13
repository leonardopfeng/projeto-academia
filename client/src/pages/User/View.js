import React from 'react';
import { useNavigate } from 'react-router-dom';
import DataView from '../../components/molecules/DataView';
import SearchFilter from '../../components/molecules/SearchFilter';
import api from "../../services/api";
import './View.css';

const UserView = () => {
  const navigate = useNavigate();
  const [users, setUsers] = React.useState([]);
  const [filteredUsers, setFilteredUsers] = React.useState([]);

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

    fetchUsers();
  }, []);

  React.useEffect(() => {
    setFilteredUsers(users);
  }, [users]);

  const handleUserClick = (user) => {
    navigate(`/user/edit/${user.key}`);
  };

  const handleDelete = async (user) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await api.delete(`/api/user/v1/${user.key}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
          }
        });
        
        setUsers(prevUsers => 
          prevUsers.filter(u => u.key !== user.key)
        );
      } catch (error) {
        console.error('Error deleting user:', error);
        alert('Failed to delete user');
      }
    }
  };

  const searchFields = [
    { key: 'userName', label: 'Username' },
    { key: 'fullname', label: 'Full Name' },
    { key: 'email', label: 'Email' },
    { key: 'roles', label: 'Role' }
  ];

  const handleFilterChange = (searchValues) => {
    const filtered = users.filter(user => {
      return Object.entries(searchValues).every(([key, value]) => {
        if (!value) return true; // if search value is empty, include all
        const fieldValue = user[key]?.toString().toLowerCase() || '';
        return fieldValue.includes(value.toLowerCase());
      });
    });
    setFilteredUsers(filtered);
  };

  return (
    <div className="user-view">
      <div className="user-view-header">
        <h1>Users</h1>
        <button 
          className="add-button"
          onClick={() => navigate('/user/add')}
        >
          Add User
        </button>
      </div>
      <SearchFilter 
        searchFields={searchFields}
        onFilterChange={handleFilterChange}
      />
      <DataView
        data={filteredUsers}
        fields={['userName', 'fullname', 'email', 'phone', 'roles']}
        onItemClick={handleUserClick}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default UserView; 