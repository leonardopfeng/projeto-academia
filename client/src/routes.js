import React from "react";
import { BrowserRouter, Route, Routes, useLocation} from "react-router-dom";

import Login from "./pages/Login";
import Home from "./pages/Home";
import ExerciseView from "./pages/Exercise/View";
import ExerciseGroupView from "./pages/ExerciseGroup/View";
import ExerciseForm from "./pages/Exercise/Form";
import ExerciseGroupForm from "./pages/ExerciseGroup/Form";
import UserView from "./pages/User/View";
import UserForm from "./pages/User/Form";
import CoachView from "./pages/Coach/View";
import CoachForm from "./pages/Coach/Form";
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
                    <Route path="/exercise/view" element={<ExerciseView/>}/>
                    <Route path="/exercise/add" element={<ExerciseForm/>}/>
                    <Route path="/exercise/edit/:id" element={<ExerciseForm/>}/>
                    <Route path="/exerciseGroup/view" element={<ExerciseGroupView/>}/>
                    <Route path="/exerciseGroup/add" element={<ExerciseGroupForm/>}/>
                    <Route path="/exerciseGroup/edit/:id" element={<ExerciseGroupForm/>}/>
                    <Route path="/user/view" element={<UserView/>}/>
                    <Route path="/user/add" element={<UserForm/>}/>
                    <Route path="/user/edit/:id" element={<UserForm/>}/>
                    <Route path="/coach/view" element={<CoachView/>}/>
                    <Route path="/coach/add" element={<CoachForm/>}/>
                    <Route path="/coach/edit/:id" element={<CoachForm/>}/>
                </Routes>
            </main>
        </>
    );
}