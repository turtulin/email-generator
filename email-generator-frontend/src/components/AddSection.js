import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AddSection = () => {
  const [section, setSection] = useState('Bandi'); // Default section
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [deadline, setDeadline] = useState('');
  const [deadlineError, setDeadlineError] = useState(''); // For validation errors
  const [link, setLink] = useState('');
  const [directApplicationLink, setDirectApplicationLink] = useState('');
  const [type, setType] = useState('Nazionale'); // Optional: Keep if needed
  const [isYoung, setIsYoung] = useState(false);
  const [isUrgent, setIsUrgent] = useState(false);
  const [isLoading, setIsLoading] = useState(false); // Loading state
  const [notification, setNotification] = useState({ message: '', type: '' }); // For success/error messages
  const navigate = useNavigate();

  // Validate the date format (dd-mm-yyyy)
  const validateDate = (dateString) => {
    const regex = /^\d{2}-\d{2}-\d{4}$/; // Matches dd-mm-yyyy
    if (!regex.test(dateString)) {
      return false;
    }

    // Check if the date is valid
    const [day, month, year] = dateString.split('-');
    const date = new Date(`${year}-${month}-${day}`);
    return date.getFullYear() == year && date.getMonth() + 1 == month && date.getDate() == day;
  };

  const handleSave = () => {
    // Validate the deadline before saving
    if (deadline && !validateDate(deadline)) {
      setDeadlineError('Inserisci una data valida nel formato dd-mm-yyyy');
      return;
    }

    setIsLoading(true);
    setNotification({ message: '', type: '' }); // Clear previous notifications

    const sectionData = { section, title, description, link, directApplicationLink, type, isYoung, isUrgent, deadline };
    
    axios.post('/api/email/sections', sectionData)
      .then(() => {
        setNotification({ message: 'Sezione aggiunta con successo!', type: 'success' });
        setTimeout(() => navigate('/'), 1000); // Redirect after 1 second
      })
      .catch((error) => {
        console.error('Errore durante il salvataggio della sezione', error);
        setNotification({ message: 'Errore durante il salvataggio della sezione', type: 'error' });
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Aggiungi Sezione</h1>

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

      <label style={styles.label}>Sezione:</label>
      <select 
        value={section} 
        onChange={(e) => setSection(e.target.value)} 
        style={styles.input}
      >
        <option value="Bandi">Bandi</option>
        <option value="News">News</option>
        <option value="Iniziative Formative">Iniziative Formative</option>
      </select>
      <br />

      <label style={styles.label}>Titolo:</label>
      <input 
        type="text" 
        value={title} 
        onChange={(e) => setTitle(e.target.value)} 
        style={styles.input}
      />
      <br />

      <label style={styles.label}>Descrizione:</label>
      <textarea 
        value={description} 
        onChange={(e) => setDescription(e.target.value)} 
        style={{ ...styles.input, ...styles.textarea }}
      />
      
      <br />
      <label style={styles.label}>Scadenza:</label>
      <input 
        type="text" 
        placeholder="dd-mm-yyyy" 
        value={deadline} 
        onChange={(e) => {
          const value = e.target.value;
          // Allow only digits and hyphens, and enforce the dd-mm-yyyy format
          if (/^\d{0,2}-?\d{0,2}-?\d{0,4}$/.test(value)) {
            setDeadline(value);
            setDeadlineError('');
          }
        }}
        style={styles.input}
      />
      {deadlineError && <p style={{ color: 'red', marginTop: '5px' }}>{deadlineError}</p>}
      <br />

      <label style={styles.label}>Link al bando:</label>
      <input 
        type="text" 
        value={link} 
        onChange={(e) => setLink(e.target.value)} 
        style={styles.input}
      />
      <br />

      <label style={styles.label}>Link per candidatura:</label>
      <input 
        type="text" 
        value={directApplicationLink} 
        onChange={(e) => setDirectApplicationLink(e.target.value)} 
        style={styles.input}
      />
      <br />

      <label style={styles.label}>Tipo:</label>
      <select 
        value={type} 
        onChange={(e) => setType(e.target.value)} 
        style={styles.input}
      >
        <option value="Nazionale">Nazionale</option>
        <option value="Internazionale">Internazionale</option>
      </select>
      <br />

      <label style={styles.label}>Giovani:</label>
      <input 
        type="checkbox" 
        checked={isYoung} 
        onChange={(e) => setIsYoung(e.target.checked)} 
        style={styles.checkbox}
      />
      <br />

      <label style={styles.label}>Urgente:</label>
      <input 
        type="checkbox" 
        checked={isUrgent} 
        onChange={(e) => setIsUrgent(e.target.checked)} 
        style={styles.checkbox}
      />
      <br />

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

export default AddSection;

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
  label: {
    display: 'block',
    marginBottom: '5px',
    fontWeight: 'bold',
  },
  input: {
    width: '100%',
    padding: '10px',
    fontSize: '14px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    marginBottom: '15px',
  },
  textarea: {
    resize: 'vertical', // Allow vertical resizing
    minHeight: '100px',
  },
  checkbox: {
    marginBottom: '15px',
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