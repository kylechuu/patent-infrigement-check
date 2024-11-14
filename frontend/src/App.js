import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [patentId, setPatentId] = useState('');
    const [companyName, setCompanyName] = useState('');
    const [response, setResponse] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const result = await axios.post('http://localhost:8080/api/v1/generate-report', {
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
        }
    };

  return (
    <div>
        <h1>Patent Infriginment Check</h1>
        <h1>Please input the required fields to generate report</h1>
        <form onSubmit={handleSubmit}>
            <div>
                <label>Patent ID:</label>
                <input type="text" value={patentId} onChange={(e) => setPatentId(e.target.value)} />
            </div>
            <div>
                <label>Company Name:</label>
                <input type="text" value={companyName} onChange={(e) => setCompanyName(e.target.value)} />
            </div>
            <button type="submit">submit</button>
        </form>
        {response && (
            <div>
                <h2>Company Details:</h2>
                <p>ID: {response.patent_id}</p>
                <p>Name: {response.company_name}</p>
                <p>Details: {response.overall_risk_assessment}</p>
            </div>
        )}
    </div>
  );
}

export default App;
