import React, { useState } from 'react';
import axios from 'axios';
import MoonLoader from 'react-spinners/MoonLoader';
import './Report.css'

function Report() {
    const [reports, setReports] = useState([]);
    const [loading, setLoading] = useState(false);

    const fetchReports = async () => {
        setLoading(true);
        setReports([]);

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

    // Delete a report by ID
    const deleteReport = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/v1/report/${id}`, {
                headers: { 'Content-Type': 'application/json' },
            });
            alert(`Report with ID: ${id} deleted successfully!`);
            setReports((prevReports) => prevReports.filter((report) => report.id !== id));
        } catch (error) {
            console.error(`Error deleting report with ID ${id}:`, error);
            alert('Failed to delete report. Please try again.');
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

            {reports.length > 0 && (
                <div className="reports-container">
                    {reports.map((report) => (
                        <div key={report.id} className="report-item">
                            <pre>{JSON.stringify(report, null, 2)}</pre>
                            <button 
                                className="delete-button"
                                onClick={() => deleteReport(report.id)}
                            >
                                Delete
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Report;
