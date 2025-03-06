import React from "react";
import './global.css';
import AppRoutes from "./routes";
import { BrowserRouter } from "react-router-dom";

export default function App() {
  return(
    <BrowserRouter>
      <AppRoutes/>
    </BrowserRouter>
  );
}

