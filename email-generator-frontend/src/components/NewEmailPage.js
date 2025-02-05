import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const NewEmailPage = () => {
  const [emailHtml, setEmailHtml] = useState('');
  const [notification, setNotification] = useState({ message: '', type: '' }); // For success/error messages

  useEffect(() => {
    let isMounted = true; // Track if the component is still mounted

    // Fetch the HTML template from the backend whenever the component loads
    axios.get('/api/email/generate-email')
      .then(response => {
        if (isMounted) {
          setEmailHtml(response.data);
        }
      })
      .catch(error => {
        if (isMounted) {
          console.error("Error fetching the email template", error);
          setNotification({ message: 'Errore durante il caricamento del template della mail', type: 'error' });
        }
      });

    return () => {
      isMounted = false; // Cleanup to avoid setting state on an unmounted component
    };
  }, []); // Only on initial load or on reload

  const handleCopy = async () => {
    try {
      if (navigator.clipboard && window.ClipboardItem) {
        await navigator.clipboard.write([
          new ClipboardItem({
            'text/html': new Blob([emailHtml], { type: 'text/html' }),
            'text/plain': new Blob([emailHtml], { type: 'text/plain' })
          })
        ]);
        setNotification({ message: 'Template della mail copiato negli appunti come contenuto formattato!', type: 'success' });
      } else {
        // Create a content-editable div for copying rich content
        const tempDiv = document.createElement('div');
        tempDiv.style.position = 'fixed'; // Prevent scrolling to bottom of page
        tempDiv.style.opacity = '0'; // Hide the div
        tempDiv.setAttribute('contenteditable', 'true'); // Make it content-editable
        tempDiv.innerHTML = emailHtml; // Set HTML content
        document.body.appendChild(tempDiv);
        tempDiv.focus();
        
        // Select all content in the div
        document.execCommand('selectAll', false, null);
        document.execCommand('copy');
        
        setNotification({ message: 'Template della mail copiato negli appunti usando un metodo alternativo.', type: 'info' });

        // Clean up by removing the temp div
        document.body.removeChild(tempDiv);
      }
    } catch (error) {
      console.error("Failed to copy email template:", error);
      setNotification({ message: 'Errore durante la copia del template della mail. Riprova.', type: 'error' });
    }
  };

  const handleNewEmail = () => {
    // Call the backend to clear all sections
    axios.post('/api/email/clear-sections')
      .then(() => {
        setNotification({ message: 'Tutte le sezioni sono state rimosse. La mail è stata resettata.', type: 'success' });
        setTimeout(() => window.location.reload(), 1000); // Reload after 1 second
      })
      .catch(error => {
        console.error("Errore durante la rimozione delle sezioni", error);
        setNotification({ message: 'Errore durante la rimozione delle sezioni.', type: 'error' });
      });
  };

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Configurazione della mail</h1>

      {/* Notification */}
      {notification.message && (
        <div style={{
          ...styles.notification,
          backgroundColor: notification.type === 'success' ? '#d4edda' : 
                          notification.type === 'error' ? '#f8d7da' : '#fff3cd',
          color: notification.type === 'success' ? '#155724' : 
                 notification.type === 'error' ? '#721c24' : '#856404',
        }}>
          {notification.message}
        </div>
      )}

      <div style={styles.buttonGroup}>
        <Link to="/edit-introduction" style={styles.link}>
          <button style={styles.button}>Modifica Introduzione</button>
        </Link>
        <Link to="/edit-conclusion" style={styles.link}>
          <button style={styles.button}>Modifica Conclusione</button>
        </Link>
        <Link to="/edit-footer" style={styles.link}>
          <button style={styles.button}>Modifica Footer</button>
        </Link>
        <Link to="/edit-contacts" style={styles.link}>
          <button style={styles.button}>Modifica Contatti</button>
        </Link>
      </div>
      <div style={styles.buttonGroup}>
        <Link to="/add-section" style={styles.link}>
          <button style={styles.button}>Crea nuova sezione</button>
        </Link>
      </div>
      <div style={styles.buttonGroup}>
        <button onClick={handleCopy} style={styles.buttonPrimary}>Copia Mail</button>
        <button onClick={handleNewEmail} style={styles.buttonDanger}>Genera una Nuova Mail</button>
      </div>

      <h2 style={styles.subtitle}>Anteprima: </h2>

      <div 
        dangerouslySetInnerHTML={{ __html: emailHtml }} 
        style={styles.emailPreview}
      />
    </div>
  );
};

export default NewEmailPage;

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
  subtitle: {
    fontSize: '20px',
    color: '#333',
    marginTop: '20px',
    marginBottom: '10px',
  },
  notification: {
    padding: '10px',
    borderRadius: '5px',
    marginBottom: '20px',
    textAlign: 'center',
  },
  buttonGroup: {
    display: 'flex',
    justifyContent: 'center',
    gap: '10px',
    marginBottom: '20px',
  },
  button: {
    padding: '10px 20px',
    fontSize: '14px',
    color: '#333',
    backgroundColor: '#f1f5f8',
    border: '1px solid #ccc',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s ease',
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
  buttonDanger: {
    padding: '10px 20px',
    fontSize: '14px',
    color: '#fff',
    backgroundColor: '#dc3545',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    transition: 'background-color 0.3s ease',
  },
  link: {
    textDecoration: 'none',
  },
  emailPreview: {
    border: '1px solid #ddd',
    padding: '20px',
    borderRadius: '5px',
    backgroundColor: '#fff',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
  },
};