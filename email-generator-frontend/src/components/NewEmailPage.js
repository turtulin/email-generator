import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const NewEmailPage = () => {
  const [emailHtml, setEmailHtml] = useState('');

  useEffect(() => {
    // Fetch the HTML template from the backend whenever the component loads
    axios.get('/api/email/generate-email')
      .then(response => {
        setEmailHtml(response.data);
      })
      .catch(error => {
        console.error("Error fetching the email template", error);
      });
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
        alert("Email template copied to clipboard as formatted content!");
      } else {
        alert("Your browser doesn't fully support the latest clipboard API. Using fallback method.");

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
        
        alert("Email template copied to clipboard using fallback method.");

        // Clean up by removing the temp div
        document.body.removeChild(tempDiv);
      }
    } catch (error) {
      console.error("Failed to copy email template:", error);
      alert("Failed to copy the email template. Please try again.");
    }
  };

  return (
    <div>
      <h1>Genera una nuova mail</h1>
      <Link to="/edit-introduction">
        <button>Modifica Introduzione</button>
      </Link>
      <Link to="/edit-conclusion">
        <button>Modifica Conclusione</button>
      </Link>
      <Link to="/edit-footer">
        <button>Modifica Footer</button>
      </Link>
      <Link to="/edit-contacts">
        <button>Modifica Contatti</button>
      </Link>
      <br /><br />
      <Link to="/add-section">
        <button>Crea nuova sezione</button>
      </Link>
      <br /><br />
      <button onClick={handleCopy}>Copia Mail</button>

      <h2>Anteprima della Mail</h2>

      <div 
        dangerouslySetInnerHTML={{ __html: emailHtml }} 
        style={{ border: '1px solid #ddd', padding: '10px' }}
      />

    </div>
  );
};

export default NewEmailPage;
