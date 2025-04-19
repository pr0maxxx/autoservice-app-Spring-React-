import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from './context/AuthContext'; 
import React from 'react';
import Header from "./components/Header";
import Home from "./pages/Home";
import Services from "./pages/Services";
import About from "./pages/About";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Profile from "./pages/Profile";
import EditProfile from "./pages/EditProfile";
import EditCar from "./pages/EditCar";
import ServiceManagement from "./pages/ServiceManagement";
import EditService from "./pages/EditService";
import AdminUsers from "./pages/AdminUsers";

const App = () => {
  return (
    <AuthProvider> 
      <Router>
        <Header />
        <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/services" element={<Services />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/edit-profile" element={<EditProfile />} />
        <Route path="/edit-car/:id" element={<EditCar />} />
        <Route path="/admin/services" element={<ServiceManagement />} />
        <Route path="/admin/users" element={<AdminUsers />} />
        <Route path="/edit-service/:id" element={<EditService />} />
        
        </Routes>
      </Router>
    </AuthProvider>
  );
};

export default App;
