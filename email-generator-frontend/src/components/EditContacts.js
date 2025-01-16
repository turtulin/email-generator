import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const EditContacts = () => {
  const [contacts, setContacts] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch the current contacts from the backend
    axios.get('/api/email/contacts')
      .then(response => {
        setContacts(response.data);
      })
      .catch(error => {
        console.error("Error fetching the contacts", error);
      });
  }, []);

  const handleSave = () => {
    // Save the updated contacts to the backend
    axios.post('/api/email/contacts', contacts, {
      headers: { 'Content-Type': 'text/plain' }
    })
      .then(() => {
        alert("Contacts updated!");
        navigate('/'); // Redirect back to the NewEmailPage
      })
      .catch(error => {
        console.error("Error updating the contacts", error);
      });
  };

  return (
    <div>
      <h1>Modifica Contatti</h1>
      <textarea
        value={contacts}
        onChange={(e) => setContacts(e.target.value)}
        rows="5"
        cols="50"
      />
      <br />
      <button onClick={handleSave}>Salva</button>
      <button onClick={() => navigate('/')}>Indietro</button>
    </div>
  );
};

export default EditContacts;
