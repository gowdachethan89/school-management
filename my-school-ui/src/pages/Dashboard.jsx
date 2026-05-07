import React, { useEffect, useState } from 'react';
import { studentService } from '../services/api';
import { useAuth } from '../context/AuthContext';

const Dashboard = () => {
  const [students, setStudents] = useState([]);
  const [form, setForm] = useState({ name: '', age: '' });
  const { logout, user } = useAuth();

  useEffect(() => { loadStudents(); }, []);

  const loadStudents = async () => {
    const res = await studentService.getAll();
    setStudents(res.data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await studentService.create(form);
    setForm({ name: '', age: '' });
    loadStudents();
  };

  const handleDelete = async (id) => {
    await studentService.delete(id);
    loadStudents();
  };

  return (
    <div style={{ padding: '20px' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h2>Welcome, {user.username}</h2>
        <button onClick={logout}>Logout</button>
      </header>

      <form onSubmit={handleSubmit} style={{ margin: '20px 0' }}>
        <input placeholder="Name" value={form.name} onChange={e => setForm({...form, name: e.target.value})} />
        <input placeholder="Age" type="number" value={form.age} onChange={e => setForm({...form, age: e.target.value})} />
        <button type="submit">Add Student</button>
      </form>

      <table border="1" width="100%">
        <thead>
          <tr><th>ID</th><th>Name</th><th>Age</th><th>Actions</th></tr>
        </thead>
        <tbody>
          {students.map(s => (
            <tr key={s.id}>
              <td>{s.id}</td>
              <td>{s.name}</td>
              <td>{s.age}</td>
              <td><button onClick={() => handleDelete(s.id)}>Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Dashboard;