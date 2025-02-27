import React from "react";
import './InputField.css';

export const InputField = ({label, type = 'text', value, onChange, placeholder, className = '', ...props}) => {
    return(
        <div className={`input-field ${className}`} {...props}>
            {label && <label className="input-label">{label}</label>}
            <input
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                className="input"
            >
            </input>
        </div>

    );

};