import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

const API_BASE_URL = "http://localhost:8080/students";

function App() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingStudent, setEditingStudent] = useState(null);
  
  // NEW: State for creating a student
  const [newStudent, setNewStudent] = useState({ name: '', age: '' });

  // 1. GET ALL STUDENTS
  const fetchStudents = async () => {
    try {
      const response = await axios.get(API_BASE_URL);
      setStudents(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching students:", error);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStudents();
  }, []);

  // 2. CREATE STUDENT (New API call)
  const createStudent = async (e) => {
    e.preventDefault();
    if (!newStudent.name || !newStudent.age) {
      alert("Please fill in both name and age");
      return;
    }
    try {
      await axios.post(API_BASE_URL, newStudent);
      setNewStudent({ name: '', age: '' }); // Reset form
      fetchStudents(); // Refresh table
    } catch (error) {
      console.error("Error creating student:", error);
    }
  };

  // 3. DELETE STUDENT
  const deleteStudent = async (id) => {
    if (window.confirm("Are you sure you want to delete this student?")) {
      await axios.delete(`${API_BASE_URL}/${id}`);
      fetchStudents();
    }
  };

  // 4. UPDATE STUDENT
  const handleUpdate = async (e) => {
    e.preventDefault();
    await axios.put(`${API_BASE_URL}/${editingStudent.id}`, editingStudent);
    setEditingStudent(null);
    fetchStudents();
  };

  if (loading) return <div className="loading">Loading School Data...</div>;

  return (
    <div className="app-container">
      <h1>School Management</h1>

      {/* CREATE STUDENT FORM */}
      <div className="form-container">
        <h3>Add New Student</h3>
        <form onSubmit={createStudent}>
          <input 
            type="text"
            placeholder="Student Name"
            value={newStudent.name}
            onChange={(e) => setNewStudent({...newStudent, name: e.target.value})}
            className="form-input"
          />
          <input 
            type="number"
            placeholder="Age"
            value={newStudent.age}
            onChange={(e) => setNewStudent({...newStudent, age: e.target.value})}
            className="form-input"
          />
          <button type="submit" className="btn-primary">
            Add Student
          </button>
        </form>
      </div>

      <hr />

      {/* UPDATE FORM SECTION (Appears only when editing) */}
      {editingStudent && (
        <div className="edit-container">
          <h3>Edit Student</h3>
          <form onSubmit={handleUpdate}>
            <input 
              value={editingStudent.name} 
              onChange={(e) => setEditingStudent({...editingStudent, name: e.target.value})}
              className="form-input-edit"
            />
            <input 
              value={editingStudent.age} 
              onChange={(e) => setEditingStudent({...editingStudent, age: e.target.value})}
              className="form-input-edit"
            />
            <button type="submit" className="btn-success">Save Changes</button>
            <button onClick={() => setEditingStudent(null)} className="btn-cancel">Cancel</button>
          </form>
        </div>
      )}

      {/* DATA TABLE */}
      <table className="data-table">
        <thead>
          <tr className="table-header-row">
            <th className="table-header-cell">ID</th>
            <th className="table-header-cell">Name</th>
            <th className="table-header-cell">Age</th>
            <th className="table-header-cell">Actions</th>
          </tr>
        </thead>
        <tbody>
          {students.map((student) => (
            <tr key={student.id} className="table-body-row">
              <td className="table-cell">{student.id}</td>
              <td className="table-cell">{student.name}</td>
              <td className="table-cell">{student.age}</td>
              <td className="table-cell">
                <button 
                  onClick={() => setEditingStudent(student)}
                  className="btn-edit"
                >
                  Edit
                </button>
                <button 
                  onClick={() => deleteStudent(student.id)}
                  className="btn-delete"
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
}

export default App;