import React from "react";
import './styles.css';

export default function Login(){
    return (
        <div className="login-container">
            <section className="form">
                <form>
                    <h1> Access your account</h1>
                    <input placeholder="Username"></input>
                    <input type="password" placeholder="Password"></input>

                    <button type="submit" className="button">Login</button>
                </form>
            </section>
        </div>

    )
}