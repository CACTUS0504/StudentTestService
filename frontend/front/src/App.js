import logo from './logo.svg';
import './App.css';
import { Route, Routes } from 'react-router-dom';
import {useEffect, useState} from "react";
import { useLocalState } from './util/useLocalState';
import Dashboard from './components/Dashboard';
import HomePage from './components/HomePage';
import LoginPage from './components/LoginPage';
import RegistrationPage from './components/RegistrationPage';
import PrivateRoute from './privateRoute/PrivateRoute';
import StudentDashboard from './student/StudentDashboard';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {

  const [jwt, setJwt] = useLocalState("", "jwt");

  useEffect(() => {
    console.log(`jwt is ${jwt}`);
  }, [jwt]
  );

  return (
    <Routes>
      <Route path="/" element={<HomePage/>}/>
      <Route path="/login" element={<LoginPage/>}/>
      <Route path="/registration" element={<RegistrationPage/>}/>
      <Route path="/dashboard" element={<PrivateRoute> <Dashboard/> </PrivateRoute>}/>
      <Route path="/student/*" element={<PrivateRoute> <StudentDashboard/> </PrivateRoute>}/>
    </Routes>
  );
}

export default App;
