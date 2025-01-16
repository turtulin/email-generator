import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const EditLogo = () => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [currentLogoUrl, setCurrentLogoUrl] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    // Set the path to display the current logo
    setCurrentLogoUrl('/logoUnicam.png');
  }, []);

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };

  const handleSave = () => {
    if (selectedFile) {
        const formData = new FormData();
        formData.append('file', selectedFile);

        axios.post('/api/email/upload-logo', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        })
        .then(() => {
            alert("Logo aggiornato con successo!");
            setCurrentLogoUrl(`/logoUnicam.png?timestamp=${new Date().getTime()}`);
        })
        .catch((error) => {
            console.error("Errore durante l'aggiornamento del logo", error);
            alert("Errore durante l'aggiornamento del logo");
        });
    } else {
        alert("Seleziona un file prima di salvare");
    }
};


  return (
    <div>
      <h1>Modifica Logo</h1>
      <p>Logo attuale:</p>
      {currentLogoUrl && <img src={currentLogoUrl} alt="Current Logo" style={{ width: '100px' }} />}
      <br />
      <input type="file" onChange={handleFileChange} />
      <br />
      <button onClick={handleSave}>Salva</button>
      <button onClick={() => navigate('/')}>Indietro</button>
    </div>
  );
};

export default EditLogo;
