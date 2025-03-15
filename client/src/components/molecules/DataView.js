import React, { useState } from 'react';
import PropTypes from 'prop-types';
import VideoModal from './VideoModal';
import './DataView.css';

const DataView = ({ data, fields, onItemClick, onDelete, showVideoButton, customActions }) => {
  const [selectedVideo, setSelectedVideo] = useState(null);

  const formatFieldValue = (field, value) => {
    if (Array.isArray(value)) {
      return value.join(', ');
    }
    
    if (field === 'hiredDate' && value) {
      const date = new Date(value);
      return date.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      });
    }
    
    return value;
  };

  return (
    <div className="data-view">
      {data.map((item) => (
        <div key={item.key} className="data-view-item">
          <div 
            className="data-view-item-content"
            onClick={() => onItemClick(item)}
          >
            {fields.map((field) => (
              <div key={field} className="data-view-field">
                <span className="field-label">{field}:</span>
                <span className="field-value">{formatFieldValue(field, item[field])}</span>
              </div>
            ))}
          </div>
          
          <div className="data-view-item-actions">
            {showVideoButton && item.videoUrl && (
              <button
                onClick={() => setSelectedVideo(item.videoUrl)}
                className="video-button"
              >
                Video
              </button>
            )}
            <button
              onClick={(e) => {
                e.stopPropagation();
                onDelete(item);
              }}
              className="delete-button"
            >
              Delete
            </button>
            {customActions && customActions(item)}
          </div>
        </div>
      ))}

      {selectedVideo && (
        <VideoModal
          videoUrl={selectedVideo}
          onClose={() => setSelectedVideo(null)}
        />
      )}
    </div>
  );
};

DataView.propTypes = {
  data: PropTypes.arrayOf(PropTypes.object).isRequired,
  fields: PropTypes.arrayOf(PropTypes.string).isRequired,
  onItemClick: PropTypes.func,
  onDelete: PropTypes.func.isRequired,
  showVideoButton: PropTypes.bool,
  customActions: PropTypes.func,
};

DataView.defaultProps = {
  onItemClick: null,
  showVideoButton: false,
  customActions: null,
};

export default DataView; 