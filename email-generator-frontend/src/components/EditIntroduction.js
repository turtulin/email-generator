import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const EditIntroduction = () => {
  const [introduction, setIntroduction] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch the current introduction from the backend
    axios.get('/api/email/introduction')
      .then(response => {
        setIntroduction(response.data);
      })
      .catch(error => {
        console.error("Error fetching the introduction", error);
      });
  }, []);

  const handleSave = () => {
    // Save the updated introduction to the backend
    axios.post('/api/email/introduction', introduction, {
      headers: { 'Content-Type': 'text/plain' }
    })
      .then(() => {
        alert("Introduzione aggiornata!");
        navigate('/'); // Redirect back to "Genera una nuova mail"
      })
      .catch(error => {
        console.error("Error updating the introduction", error);
      });
  };

  return (
    <div>
      <h1>Modifica Introduzione</h1>
      <textarea
        value={introduction}
        onChange={(e) => setIntroduction(e.target.value)}
        rows="5"
        cols="50"
      />
      <br />
      <button onClick={handleSave}>Salva</button>
      <button onClick={() => navigate('/')}>Indietro</button> {/* Updated path */}
    </div>
  );
};

export default EditIntroduction;
