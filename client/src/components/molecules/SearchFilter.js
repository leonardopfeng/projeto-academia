import React from 'react';
import PropTypes from 'prop-types';
import './SearchFilter.css';

const SearchFilter = ({ searchFields, onFilterChange }) => {
  const [searchValues, setSearchValues] = React.useState({});

  const handleSearchChange = (field, value) => {
    const newSearchValues = {
      ...searchValues,
      [field.key]: value
    };
    setSearchValues(newSearchValues);
    onFilterChange(newSearchValues);
  };

  return (
    <div className="search-filter">
      {searchFields.map((field) => (
        <div key={field.key} className="search-field">
          <input
            type="text"
            placeholder={`Search by ${field.label}...`}
            value={searchValues[field.key] || ''}
            onChange={(e) => handleSearchChange(field, e.target.value)}
            className="search-input"
          />
        </div>
      ))}
    </div>
  );
};

SearchFilter.propTypes = {
  searchFields: PropTypes.arrayOf(
    PropTypes.shape({
      key: PropTypes.string.isRequired,
      label: PropTypes.string.isRequired,
    })
  ).isRequired,
  onFilterChange: PropTypes.func.isRequired,
};

export default SearchFilter; 