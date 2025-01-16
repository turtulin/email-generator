import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NewEmailPage from './components/NewEmailPage';
import EditIntroduction from './components/EditIntroduction';
import EditConclusion from './components/EditConclusion';
import AddSection from './components/AddSection';
import EditFooter from './components/EditFooter';
import EditContacts from './components/EditContacts';

function App() {
  const [sections, setSections] = useState([]);

  const addSection = (section) => {
    setSections((prevSections) => [...prevSections, section]);
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={<NewEmailPage sections={sections} addSection={addSection} />} />
        <Route path="/edit-introduction" element={<EditIntroduction />} />
        <Route path="/edit-conclusion" element={<EditConclusion />} />
        <Route path="/edit-footer" element={<EditFooter />} />
        <Route path="/edit-contacts" element={<EditContacts />} />
        <Route path="/add-section" element={<AddSection addSection={addSection} />} />
      </Routes>
    </Router>
  );
}

export default App;
