import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/api';

const AuthPage = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '' // Required by your User entity
  });
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isLogin) {
        // Handles login using existing AuthContext logic
        await login(formData.username, formData.password);
        navigate('/dashboard');
      } else {
        // Calls your /api/public/signup endpoint
        const response = await authService.signup(formData);
        alert(response.data); // Displays "User registered successfully!" from your backend
        setIsLogin(true); // Switch back to login mode after successful registration
      }
    } catch (err) {
      alert(isLogin ? "Login failed!" : "Registration failed!");
      console.error(err);
    }
  };

  return (
    <div style={{ padding: '50px', maxWidth: '400px', margin: 'auto', fontFamily: 'Arial' }}>
      <div style={{ textAlign: 'center', marginBottom: '20px' }}>
        <h2>{isLogin ? 'School Login' : 'Teacher Registration'}</h2>
        <p style={{ fontSize: '14px', color: '#666' }}>
          {isLogin ? "Access your dashboard" : "Register as a new teacher"}
        </p>
      </div>

      <form onSubmit={handleSubmit}>
        {!isLogin && (
          <div style={{ marginBottom: '15px' }}>
            <input 
              name="email"
              type="email" 
              placeholder="Email Address" 
              value={formData.email} 
              onChange={handleInputChange} 
              required
              style={{ width: '100%', padding: '10px', boxSizing: 'border-box' }}
            />
          </div>
        )}
        
        <div style={{ marginBottom: '15px' }}>
          <input 
            name="username"
            type="text" 
            placeholder="Username" 
            value={formData.username} 
            onChange={handleInputChange} 
            required
            style={{ width: '100%', padding: '10px', boxSizing: 'border-box' }}
          />
        </div>

        <div style={{ marginBottom: '15px' }}>
          <input 
            name="password"
            type="password" 
            placeholder="Password" 
            value={formData.password} 
            onChange={handleInputChange} 
            required
            style={{ width: '100%', padding: '10px', boxSizing: 'border-box' }}
          />
        </div>

        <button 
          type="submit" 
          style={{ 
            width: '100%', 
            padding: '12px', 
            backgroundColor: isLogin ? '#007bff' : '#28a745', 
            color: 'white', 
            border: 'none', 
            borderRadius: '4px',
            cursor: 'pointer',
            fontSize: '16px'
          }}
        >
          {isLogin ? 'Login' : 'Sign Up'}
        </button>
      </form>

      <div style={{ marginTop: '20px', textAlign: 'center' }}>
        <button 
          onClick={() => setIsLogin(!isLogin)} 
          style={{ background: 'none', border: 'none', color: '#007bff', cursor: 'pointer', textDecoration: 'underline' }}
        >
          {isLogin ? "Don't have an account? Register here" : "Already have an account? Login here"}
        </button>
      </div>
    </div>
  );
};

export default AuthPage;