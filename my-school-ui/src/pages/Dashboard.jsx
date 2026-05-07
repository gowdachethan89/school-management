import React, { useEffect, useState } from 'react';
import { studentService } from '../services/api';
import { useAuth } from '../context/AuthContext';

const Dashboard = () => {
  const [students, setStudents] = useState([]);
  const [form, setForm] = useState({ name: '', age: '' });
  const [editingId, setEditingId] = useState(null); // Track if we are editing
  const { logout, user } = useAuth();

  useEffect(() => { loadStudents(); }, []);

  const loadStudents = async () => {
    try {
      const res = await studentService.getAll();
      setStudents(res.data);
    } catch (err) {
      console.error("Failed to load students", err);
    }
  };

  // Triggered when clicking "Edit" in the table
  const startEdit = (student) => {
    setEditingId(student.id);
    setForm({ name: student.name, age: student.age });
  };

  // Cancel editing mode
  const cancelEdit = () => {
    setEditingId(null);
    setForm({ name: '', age: '' });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        // Calls PUT /api/students/{id}
        await studentService.update(editingId, form);
        setEditingId(null);
      } else {
        // Calls POST /api/students
        await studentService.create(form);
      }
      setForm({ name: '', age: '' });
      loadStudents();
    } catch (err) {
      alert("Operation failed. Check console for details.");
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Delete this student?")) {
      await studentService.delete(id); // Calls DELETE /api/students/{id}
      loadStudents();
    }
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #ccc' }}>
        <h2>School Dashboard</h2>
        <div>
          <span style={{ marginRight: '15px' }}>User: <strong>{user.username}</strong></span>
          <button onClick={logout} style={{ padding: '5px 10px', cursor: 'pointer' }}>Logout</button>
        </div>
      </header>

      <section style={{ margin: '20px 0', padding: '15px', backgroundColor: '#f9f9f9', borderRadius: '8px' }}>
        <h3>{editingId ? "Edit Student" : "Register New Student"}</h3>
        <form onSubmit={handleSubmit}>
          <input 
            placeholder="Name" 
            value={form.name} 
            onChange={e => setForm({...form, name: e.target.value})} 
            style={{ marginRight: '10px', padding: '8px' }}
            required
          />
          <input 
            placeholder="Age" 
            type="number" 
            value={form.age} 
            onChange={e => setForm({...form, age: e.target.value})} 
            style={{ marginRight: '10px', padding: '8px' }}
            required
          />
          <button type="submit" style={{ padding: '8px 15px', backgroundColor: editingId ? '#28a745' : '#007bff', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
            {editingId ? "Update Student" : "Add Student"}
          </button>
          {editingId && (
            <button type="button" onClick={cancelEdit} style={{ marginLeft: '10px', padding: '8px' }}>
              Cancel
            </button>
          )}
        </form>
      </section>

      <table border="1" width="100%" style={{ borderCollapse: 'collapse', textAlign: 'left' }}>
        <thead>
          <tr style={{ backgroundColor: '#eee' }}>
            <th style={{ padding: '10px' }}>ID</th>
            <th style={{ padding: '10px' }}>Name</th>
            <th style={{ padding: '10px' }}>Age</th>
            <th style={{ padding: '10px' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {students.map(s => (
            <tr key={s.id}>
              <td style={{ padding: '10px' }}>{s.id}</td>
              <td style={{ padding: '10px' }}>{s.name}</td>
              <td style={{ padding: '10px' }}>{s.age}</td>
              <td style={{ padding: '10px' }}>
                <button 
                  onClick={() => startEdit(s)} 
                  style={{ marginRight: '10px', color: '#007bff', cursor: 'pointer', background: 'none', border: '1px solid #007bff', borderRadius: '4px' }}
                >
                  Edit
                </button>
                <button 
                  onClick={() => handleDelete(s.id)} 
                  style={{ color: 'red', cursor: 'pointer', background: 'none', border: '1px solid red', borderRadius: '4px' }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Dashboard;