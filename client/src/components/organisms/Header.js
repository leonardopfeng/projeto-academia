import React, { useState } from "react";
import { Button } from "../atoms/Button";
import { Link } from "react-router-dom";
import "./Header.css";

export const Header = () => {
    // Use a single state to track which dropdown is open (if any)
    const [activeDropdown, setActiveDropdown] = useState(null);

    // Toggle function to handle dropdown visibility
    const toggleDropdown = (dropdown) => {
        if (activeDropdown === dropdown) {
            // If clicking the same dropdown that's already open, close it
            setActiveDropdown(null);
        } else {
            // Otherwise, open the clicked dropdown and close any other
            setActiveDropdown(dropdown);
        }
    };

    return(
        <header className="header">
            <Link to='/teste' style={{ textDecoration: 'none' }}>
                <h1 className="header-title">Projeto Academia</h1>
            </Link>
            <nav className="header-nav">
                <div className="dropdown-container">
                <button 
                    onClick={() => toggleDropdown('cadastros')}
                    className="dropdown-button"
                >
                    Cadastros
                </button>
                    {activeDropdown === 'cadastros' && (
                        <div className="dropdown-menu">
                            <Link to="/add/exercise" className="dropdown-item" onClick={() => setActiveDropdown(null)}>Cadastrar Exercício</Link>
                        </div>
                    )}
                </div>

                <div className="dropdown-container">
                <button 
                    onClick={() => toggleDropdown('listagem')}
                    className="dropdown-button"
                >
                    Listagem
                </button>
                    {activeDropdown === 'listagem' && (
                        <div className="dropdown-menu">
                            <Link to="/exercise/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>Exercício</Link>
                            <Link to="/exerciseGroup/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>Grupo de Exercício</Link>
                            <Link to="/client/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>Cliente</Link>
                        </div>
                    )}
                </div>
            </nav>

        </header>

    )
};