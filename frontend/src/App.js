import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import InputForm from './report/InputForm';
import Report from './report/Report';
import './App.css';

function App() {
    return (
        <Router>
            <div className="app-layout">
                <Sidebar />
                <div className="main-content">
                    <Routes>
                        <Route path="/" element={<InputForm />} />
                        <Route path="/reports" element={<Report />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
