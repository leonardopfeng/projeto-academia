import React, {useState} from "react";
import { useNavigate } from "react-router-dom";
import './styles.css';
import api from "../../services/api";
import { Button } from '../../components/atoms/Button'
import { InputField } from "../../components/molecules/InputField";

export default function Login(){

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    async function login(e){
        e.preventDefault();

        const data = {
            username,
            password
        };

        try {
            const response = await api.post('/auth/signin', data);

            localStorage.setItem('username', username);
            localStorage.setItem('accessToken', response.data.accessToken);

            navigate('/home');
        } catch (error) {
            alert("Username or Password invalid. Please try again");
        }
    }


    return (
        <div className="login-container">
            <section className="form">
                <form onSubmit={login}>
                    <h1> Access your account</h1>
                    <InputField
                        label="Username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        placeholder="Enter your username"
                    />
                    <InputField
                        type="password"
                        label="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        placeholder="Enter your password"
                    />

                    <Button type="submit">Login</Button>
                </form>
            </section>
        </div>

    )
}