import React, { useState } from "react";
import { Button } from "../atoms/Button";
import { Link } from "react-router-dom";

export const Header = () => {

    const [dropdownOpen, setDropdownOpen] = useState(false);

    return(
        <header className="bg-blue-600 text-white p-4 flex justify-between">
            <Link to='/teste' className="no-underline text-black">
                <h1 className="text-x1 font-bold text-white cursor-pointer">Projeto Academia</h1>
            </Link>
            <nav>
                <div className="relative inline-block text-left">
                <button 
                    onClick={() => setDropdownOpen(!dropdownOpen)}
                    className="mr-4"
                >
                    Cadastros
                </button>
                    {dropdownOpen && (
                        <div className="absolute right-0 mt-2 w-48 bg-white text-black border rounded shadow-lg">
                            <Link to="/add/exercise" className="block px-4 py-2 hover:bg-gray-200" onClick={() => setDropdownOpen(false)}>Cadastrar Exercício</Link>
                        </div>
                    )}
                </div>

            </nav>

        </header>

    )
};