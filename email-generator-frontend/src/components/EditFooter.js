import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const EditFooter = () => {
  const [footer, setFooter] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch the current footer from the backend
    axios.get('/api/email/footer')
      .then(response => {
        setFooter(response.data);
      })
      .catch(error => {
        console.error("Error fetching the footer", error);
      });
  }, []);

  const handleSave = () => {
    // Save the updated footer to the backend
    axios.post('/api/email/footer', footer, {
      headers: { 'Content-Type': 'text/plain' }
    })
      .then(() => {
        alert("Footer updated!");
        navigate('/');
      })
      .catch(error => {
        console.error("Error updating the footer", error);
      });
  };

  return (
    <div>
      <h1>Modifica Footer</h1>
      <textarea
        value={footer}
        onChange={(e) => setFooter(e.target.value)}
        rows="5"
        cols="50"
      />
      <br />
      <button onClick={handleSave}>Salva</button>
      <button onClick={() => navigate('/')}>Indietro</button>
    </div>
  );
};

export default EditFooter;
