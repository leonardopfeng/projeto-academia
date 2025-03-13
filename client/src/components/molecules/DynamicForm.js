import React from 'react';
import './DynamicForm.css';

const DynamicForm = ({ 
  fields, 
  onSubmit, 
  initialData = {}, 
  submitButtonText = 'Submit',
  title = 'Form'
}) => {
  const [formData, setFormData] = React.useState(initialData);

  React.useEffect(() => {
    setFormData(initialData);
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleCheckboxGroupChange = (name, value, checked) => {
    setFormData(prev => {
      const currentValues = prev[name] || [];
      if (checked) {
        return {
          ...prev,
          [name]: [...currentValues, value]
        };
      } else {
        return {
          ...prev,
          [name]: currentValues.filter(v => v !== value)
        };
      }
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const renderField = (field) => {
    switch (field.type) {
      case 'multiselect':
        return (
          <div className="checkbox-group">
            {field.options?.map(option => (
              <label key={option.value} className="checkbox-label">
                <input
                  type="checkbox"
                  name={field.name}
                  value={option.value}
                  checked={(formData[field.name] || []).includes(option.value)}
                  onChange={(e) => handleCheckboxGroupChange(field.name, option.value, e.target.checked)}
                  className="checkbox-input"
                />
                <span className="checkbox-text">{option.label}</span>
              </label>
            ))}
          </div>
        );
      case 'select':
        return (
          <select
            name={field.name}
            value={formData[field.name] || ''}
            onChange={handleChange}
            required={field.required}
          >
            <option value="">Select {field.label}</option>
            {field.options?.map(option => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        );
      case 'textarea':
        return (
          <textarea
            name={field.name}
            value={formData[field.name] || ''}
            onChange={handleChange}
            required={field.required}
            placeholder={field.placeholder}
          />
        );
      default:
        return (
          <input
            type={field.type || 'text'}
            name={field.name}
            value={formData[field.name] || ''}
            onChange={handleChange}
            required={field.required}
            placeholder={field.placeholder}
          />
        );
    }
  };

  return (
    <div className="dynamic-form-container">
      <h2>{title}</h2>
      <form onSubmit={handleSubmit} className="dynamic-form">
        {fields.map(field => (
          <div key={field.name} className="form-field">
            <label htmlFor={field.name}>{field.label}</label>
            {renderField(field)}
          </div>
        ))}
        <button type="submit" className="submit-button">
          {submitButtonText}
        </button>
      </form>
    </div>
  );
};

export default DynamicForm; 