import React from 'react';
import './Sidebar.css'; // Import Sidebar-specific styles

const Sidebar = () => (
  <div className="sidebar">
    <h2>Menu</h2>
    <a href="/" className="sidebar-link">Input Form</a>
    <a href="/reports" className="sidebar-link">Reports</a>
  </div>
);

export default Sidebar;

