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

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const renderField = (field) => {
    switch (field.type) {
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