import React, { useState } from 'react';
import axios from 'axios';
import { MoonLoader } from 'react-spinners';
import './InputForm.css';

function InputForm() {
    const [patentId, setPatentId] = useState('');
    const [companyName, setCompanyName] = useState('');
    const [response, setResponse] = useState(null);
    const [loading, setLoading] = useState(false); // Track the loading state

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setResponse(null);

        try {
            const result = await axios.post('https://ideological-alverta-side-project-kyle-37574475.koyeb.app/api/v1/generate-report', {
              patentId: patentId,
              companyName: companyName
            }, { headers: { 'Content-Type': 'application/json' }});
            setResponse(result.data); // Store the response in state
            console.log("Full response object:", result); // Log to inspect the structure
            console.log("Response data:", result.data); // Ensure data field contains expected JSON
            console.log("Patent ID:", result.data.patent_id);
            console.log("Company Name:", result.data.company_name);
            console.log("OverallRiskAssessment:", result.data.overall_risk_assessment);
        } catch (error) {
            console.error("Error fetching data:", error);
        } finally {
            setLoading(false); // Set loading to false when the request is finished
        }
    };

  return (
    <div className="app-container">
        <h1 className="app-title">Patent Infringement Check</h1>
        <h1 className="app-subtitle">Please input the required fields to generate report</h1>
        <form className="form-container" onSubmit={handleSubmit}>
            <div className="form-group">
                <label htmlFor="patentId">Patent ID:</label>
                <input type="text" id="patentId" value={patentId} onChange={(e) => setPatentId(e.target.value)} className="form-input" placeholder="Enter Patent ID" />
            </div>
            <div className="form-group">
                <label htmlFor="companyName">Company Name:</label>
                <input type="text" id="companyName" value={companyName} onChange={(e) => setCompanyName(e.target.value)} className="form-input" placeholder="Enter Company Name"/>
            </div>
            <button type="submit" className="submit-button" disabled={loading} >{loading ? 'Generating Report...' : 'Submit'}</button>
        </form>

        {/* Show the spinner when loading */}
        {loading && (
            <div className="spinner-container">
                <MoonLoader color="#57dc2f" size={50} />
            </div>
        )}

        {/* Show response once the API request is completed */}
        {response && (
            <div className="response-container">
                <h1>Infringement Report</h1>
                <pre className="response-content">{JSON.stringify(response, null, 2)}</pre>
            </div>
        )}
    </div>
  );
}

export default InputForm;