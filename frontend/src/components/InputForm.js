import React, { useState } from 'react';
import axios from 'axios';
import { MoonLoader } from 'react-spinners';
import './InputForm.css';

function InputForm() {
    const [patentId, setPatentId] = useState('');
    const [companyName, setCompanyName] = useState('');
    const [response, setResponse] = useState(null);
    const [loading, setLoading] = useState(false); // Track the loading state
    const [showModal, setShowModal] = useState(false); // Modal visibility state
    const [saving, setSaving] = useState(false); // Track saving state

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setResponse(null);

        try {
            const result = await axios.post('https://patent-infringement-check-17325157242.us-central1.run.app/api/v1/generate-report', {
              patentId: patentId,
              companyName: companyName
            }, { headers: { 'Content-Type': 'application/json' }});
            setResponse(result.data); // Store the response in state
            setShowModal(true); // Show the modal after a successful response
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

    const handleSaveReport = async () => {
        setSaving(true);
        try {
            await axios.put('https://patent-infringement-check-17325157242.us-central1.run.app/api/v1/report', response, {
                headers: { 'Content-Type': 'application/json' }
            });
            alert('Report saved successfully!');
        } catch (error) {
            console.error("Error saving report:", error);
            alert('Failed to save report');
        } finally {
            setSaving(false);
            setShowModal(false); // Close the modal after saving
        }
    };

    const handleCloseModal = () => {
        setShowModal(false); // Close the modal without saving
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

        {/* Modal for saving the report */}
        {showModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Report Generated</h2>
                        <pre className="json-display">{JSON.stringify(response, null, 2)}</pre>
                        <div className="modal-actions">
                            <button className="save-button" onClick={handleSaveReport} disabled={saving}>
                                {saving ? 'Saving...' : 'Save'}
                            </button>
                            <button className="close-button" onClick={handleCloseModal}>Close</button>
                        </div>
                    </div>
                </div>
            )}
    </div>
  );
}

export default InputForm;