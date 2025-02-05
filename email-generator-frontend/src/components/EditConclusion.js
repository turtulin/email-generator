import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const EditConclusion = () => {
  const [conclusion, setConclusion] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [notification, setNotification] = useState({ message: '', type: '' }); // For success/error messages
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch the current conclusion from the backend
    axios.get('/api/email/conclusion')
      .then(response => {
        setConclusion(response.data);
      })
      .catch(error => {
        console.error("Error fetching the conclusion", error);
        setNotification({ message: 'Errore durante il caricamento della conclusione', type: 'error' });
      });
  }, []);

  const handleSave = () => {
    setIsLoading(true);
    setNotification({ message: '', type: '' }); // Clear previous notifications

    // Save the updated conclusion to the backend
    axios.post('/api/email/conclusion', conclusion, {
      headers: { 'Content-Type': 'text/plain' }
    })
      .then(() => {
        setNotification({ message: 'Conclusione aggiornata con successo!', type: 'success' });
        setTimeout(() => navigate('/'), 1000); // Redirect after 1 second
      })
      .catch(error => {
        console.error("Error updating the conclusion", error);
        setNotification({ message: 'Errore durante l\'aggiornamento della conclusione', type: 'error' });
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Modifica Conclusione</h1>

      {/* Notification */}
      {notification.message && (
        <div style={{
          ...styles.notification,
          backgroundColor: notification.type === 'success' ? '#d4edda' : '#f8d7da',
          color: notification.type === 'success' ? '#155724' : '#721c24',
        }}>
          {notification.message}
        </div>
      )}

      <textarea
        value={conclusion}
        onChange={(e) => setConclusion(e.target.value)}
        rows="10"
        style={styles.textarea}
      />

      <div style={styles.buttonGroup}>
        <button 
          onClick={handleSave} 
          style={styles.buttonPrimary}
          disabled={isLoading}
        >
          {isLoading ? 'Salvataggio in corso...' : 'Salva'}
        </button>
        <button 
          onClick={() => navigate('/')} 
          style={styles.buttonSecondary}
        >
          Indietro
        </button>
      </div>
    </div>
  );
};

export default EditConclusion;

// Styles
const styles = {
  container: {
    padding: '20px',
    fontFamily: 'Arial, sans-serif',
    maxWidth: '800px',
    margin: '0 auto',
  },
  title: {
    fontSize: '24px',
    color: '#333',
    marginBottom: '20px',
    textAlign: 'center',
  },
  notification: {
    padding: '10px',
    borderRadius: '5px',
    marginBottom: '20px',
    textAlign: 'center',
  },
  textarea: {
    width: '100%',
    padding: '10px',
    fontSize: '14px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    marginBottom: '20px',
    resize: 'vertical', // Allow vertical resizing
  },
  buttonGroup: {
    display: 'flex',
    justifyContent: 'center',
    gap: '10px',
  },
  buttonPrimary: {
    padding: '10px 20px',
    fontSize: '14px',
    color: '#fff',
    backgroundColor: '#007bff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s ease',
  },
  buttonSecondary: {
    padding: '10px 20px',
    fontSize: '14px',
    color: '#333',
    backgroundColor: '#f1f5f8',
    border: '1px solid #ccc',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s ease',
  },
};