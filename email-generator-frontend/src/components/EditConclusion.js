import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const EditConclusion = () => {
  const [conclusion, setConclusion] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch the current conclusion from the backend
    axios.get('/api/email/conclusion')
      .then(response => {
        setConclusion(response.data);
      })
      .catch(error => {
        console.error("Error fetching the conclusion", error);
      });
  }, []);

  const handleSave = () => {
    // Save the updated conclusion to the backend
    axios.post('/api/email/conclusion', conclusion, {
      headers: { 'Content-Type': 'text/plain' }
    })
      .then(() => {
        alert("Conclusione aggiornata!");
        navigate('/'); // Redirect back to "Genera una nuova mail"
      })
      .catch(error => {
        console.error("Error updating the conclusion", error);
      });
  };

  return (
    <div>
      <h1>Modifica Conclusione</h1>
      <textarea
        value={conclusion}
        onChange={(e) => setConclusion(e.target.value)}
        rows="5"
        cols="50"
      />
      <br />
      <button onClick={handleSave}>Salva</button>
      <button onClick={() => navigate('/')}>Indietro</button> {/* Updated path */}
    </div>
  );
};

export default EditConclusion;
