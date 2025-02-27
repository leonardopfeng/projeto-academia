import React from "react";
import './Button.css'

export const Button = ({children, type = "button", onClick, className = ""}) => {
    return (
        <button
            type = {type}
            onClick = {onClick}
            className = {`button ${className}`}
        >
            {children}
        </button>
    );
};