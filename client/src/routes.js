import React from "react";
import { BrowserRouter, Route, Routes, useLocation} from "react-router-dom";

import Login from "./pages/Login";
import Home from "./pages/Home";
import { Header } from "./components/organisms/Header";

export default function AppRoutes(){
    const location = useLocation();
    const showHeader = location.pathname !== '/';

    return (
        <>
            {showHeader && <Header/>}
            <main className="p-4">
                <Routes>
                    <Route path="/" element={<Login/>}/>
                    <Route path="/home" element={<Home/>}/>
                </Routes>
            </main>
        </>
    );
}