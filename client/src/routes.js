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
import ClientView from "./pages/Client/View";
import ClientForm from "./pages/Client/Form";
import TrainingSessionView from "./pages/TrainingSession/View";
import TrainingSessionForm from "./pages/TrainingSession/Form";
import ExercisesForm from "./pages/TrainingSession/ExercisesForm";
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
                    <Route path="user">
                        <Route path="view" element={<UserView/>}/>
                        <Route path="add" element={<UserForm/>}/>
                        <Route path="edit/:id" element={<UserForm/>}/>
                    </Route>
                    <Route path="client">
                        <Route path="view" element={<ClientView/>}/>
                        <Route path="add" element={<ClientForm/>}/>
                        <Route path="edit/:id" element={<ClientForm/>}/>
                    </Route>
                    <Route path="coach">
                        <Route path="view" element={<CoachView/>}/>
                        <Route path="add" element={<CoachForm/>}/>
                        <Route path="edit/:id" element={<CoachForm/>}/>
                    </Route>
                    <Route path="exercise">
                        <Route path="view" element={<ExerciseView/>}/>
                        <Route path="add" element={<ExerciseForm/>}/>
                        <Route path="edit/:id" element={<ExerciseForm/>}/>
                    </Route>
                    <Route path="exerciseGroup">
                        <Route path="view" element={<ExerciseGroupView/>}/>
                        <Route path="add" element={<ExerciseGroupForm/>}/>
                        <Route path="edit/:id" element={<ExerciseGroupForm/>}/>
                    </Route>
                    <Route path="trainingSession">
                        <Route path="view" element={<TrainingSessionView/>}/>
                        <Route path="add" element={<TrainingSessionForm/>}/>
                        <Route path="edit/:id" element={<TrainingSessionForm/>}/>
                        <Route path=":sessionId/exercises" element={<ExercisesForm/>}/>
                    </Route>
                </Routes>
            </main>
        </>
    );
}