import React, { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import "./Header.css";

export const Header = () => {
    const [activeDropdown, setActiveDropdown] = useState(null);
    const dropdownRef = useRef(null);

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setActiveDropdown(null);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const toggleDropdown = (dropdown) => {
        setActiveDropdown(activeDropdown === dropdown ? null : dropdown);
    };

    return(
        <header className="header">
            <Link to='/home' className="header-logo-link">
                <h1 className="header-title">Projeto Academia</h1>
            </Link>
            <nav className="header-nav" ref={dropdownRef}>
                <div className="dropdown-container">
                    <button 
                        onClick={() => toggleDropdown('cadastros')}
                        className={`dropdown-button ${activeDropdown === 'cadastros' ? 'active' : ''}`}
                    >
                        Cadastros
                    </button>
                    {activeDropdown === 'cadastros' && (
                        <div className="dropdown-menu">
                            <Link to="/exercise/add" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Cadastrar Exercício
                            </Link>
                            <Link to="/exerciseGroup/add" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Cadastrar Grupo
                            </Link>
                            <Link to="/user/add" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Cadastrar Usuário
                            </Link>
                            <Link to="/coach/add" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Cadastrar Treinador
                            </Link>
                            <Link to="/client/add" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Cadastrar Cliente
                            </Link>
                            <Link to="/trainingSession/add" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Cadastrar Treino
                            </Link>
                        </div>
                    )}
                </div>

                <div className="dropdown-container">
                    <button 
                        onClick={() => toggleDropdown('listagem')}
                        className={`dropdown-button ${activeDropdown === 'listagem' ? 'active' : ''}`}
                    >
                        Listagem
                    </button>
                    {activeDropdown === 'listagem' && (
                        <div className="dropdown-menu">
                            <Link to="/exercise/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Exercícios
                            </Link>
                            <Link to="/exerciseGroup/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Grupos de Exercício
                            </Link>
                            <Link to="/user/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Usuários
                            </Link>
                            <Link to="/coach/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Treinadores
                            </Link>
                            <Link to="/client/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Clientes
                            </Link>
                            <Link to="/trainingSession/view" className="dropdown-item" onClick={() => setActiveDropdown(null)}>
                                Treinos
                            </Link>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
};