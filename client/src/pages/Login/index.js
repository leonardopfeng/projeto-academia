import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import './styles.css';
import api from "../../services/api";
import { Button } from '../../components/atoms/Button';
import { InputField } from "../../components/molecules/InputField";

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const navigate = useNavigate();

    async function login(e) {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const response = await api.post('/auth/signin', {
                username,
                password
            });

            localStorage.setItem('username', username);
            localStorage.setItem('accessToken', response.data.accessToken);

            navigate('/home');
        } catch (error) {
            setError('Username or password invalid. Please try again.');
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <div className="login-container">
            <section className="form">
                <form onSubmit={login}>
                    <h1>Welcome Back!</h1>
                    <InputField
                        label="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        placeholder="Enter your username"
                        required
                    />
                    <InputField
                        type="password"
                        label="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        placeholder="Enter your password"
                        required
                    />
                    {error && <div className="error-message">{error}</div>}
                    <Button 
                        type="submit" 
                        disabled={isLoading}
                    >
                        {isLoading ? 'Logging in...' : 'Login'}
                    </Button>
                </form>
            </section>
        </div>
    );
}