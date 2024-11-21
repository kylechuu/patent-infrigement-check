import React, { useState } from 'react';
import axios from 'axios';
import MoonLoader from 'react-spinners/MoonLoader';
import './Report.css'

function Report() {
    const [reports, setReports] = useState(null);
    const [loading, setLoading] = useState(false);

    const fetchReports = async () => {
        setLoading(true);
        setReports(null);

        try {
            const result = await axios.get('http://localhost:8080/api/v1/reports', {
                headers: { 'Content-Type': 'application/json' },
            });
            setReports(result.data);
        } catch (error) {
            console.error("Error fetching reports:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="main-content">
            <h1>All Reports</h1>
            <button className="fetch-button" onClick={fetchReports}>Fetch All Reports</button>

            {loading && (
                <div className="spinner-container">
                    <MoonLoader color="#57dc2f" size={50} />
                </div>
            )}

            {reports && (
                <div className="reports-container">
                    <pre>{JSON.stringify(reports, null, 2)}</pre>
                </div>
            )}
        </div>
    );
}

export default Report;
