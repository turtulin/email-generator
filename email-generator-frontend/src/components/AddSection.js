import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AddSection = () => {
  const [section, setSection] = useState('Bandi'); // Default section
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [link, setLink] = useState('');
  const [type, setType] = useState('Nazionale'); // Optional: Keep if needed
  const [isYoung, setIsYoung] = useState(false);
  const [deadline, setDeadline] = useState('');
  const navigate = useNavigate();

  const formatDate = (dateString) => {
    if (!dateString) return '';
    const [month, day, year] = dateString.split('/');
    return `${day}-${month}-${year}`;
  };

  const handleSave = () => {
    const formattedDeadline = formatDate(deadline);
    const sectionData = { section, title, description, link, type, isYoung, deadline: formattedDeadline };
    
    axios.post('/api/email/sections', sectionData)
      .then(() => {
        alert('Sezione aggiunta con successo');
        navigate('/');
      })
      .catch((error) => {
        console.error('Errore durante il salvataggio della sezione', error);
        alert('Errore durante il salvataggio della sezione');
      });
  };

  return (
    <div>
      <h1>Aggiungi Sezione</h1>
      <label>Sezione:</label>
      <select value={section} onChange={(e) => setSection(e.target.value)}>
        <option value="Bandi">Bandi</option>
        <option value="News">News</option>
        <option value="Altro">Altro</option>
      </select>
      <br />
      <label>Titolo:</label>
      <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} />
      <br />
      <label>Descrizione:</label>
      <textarea value={description} onChange={(e) => setDescription(e.target.value)} />
      <br />
      <label>Link:</label>
      <input type="text" value={link} onChange={(e) => setLink(e.target.value)} />
      <br />
      <label>Tipo:</label>
      <select value={type} onChange={(e) => setType(e.target.value)}>
        <option value="Nazionale">Nazionale</option>
        <option value="Internazionale">Internazionale</option>
      </select>
      <br />
      <label>Giovani:</label>
      <input type="checkbox" checked={isYoung} onChange={(e) => setIsYoung(e.target.checked)} />
      <br />
      <label>Scadenza:</label>
      <input 
        type="text" 
        placeholder="mm/dd/yyyy" 
        value={deadline} 
        onChange={(e) => setDeadline(e.target.value)} 
      />
      <br />
      <button onClick={handleSave}>Salva</button>
      <button onClick={() => navigate('/')}>Indietro</button>
    </div>
  );
};

export default AddSection;
