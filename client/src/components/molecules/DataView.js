import React, { useState } from 'react';
import PropTypes from 'prop-types';
import VideoModal from './VideoModal';
import './DataView.css';

const DataView = ({ data, fields, onItemClick, onDelete, showVideoButton }) => {
  const [selectedVideo, setSelectedVideo] = useState(null);

  return (
    <div className="data-view">
      {data.map((item) => (
        <div key={item.key} className="data-view-item">
          <div 
            className="data-view-item-content"
            onClick={() => onItemClick(item)}
          >
            {fields.map((field) => (
              <div key={field}>
                {field === 'name' && item[field]}
                {field === 'groupId' && `Group: ${item[field]}`}
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
};

DataView.defaultProps = {
  onItemClick: null,
  showVideoButton: false,
};

export default DataView; 