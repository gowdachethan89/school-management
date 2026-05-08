import axios from 'axios';

const api = axios.create({
  baseURL: 'https://school-management-c5dg.onrender.com',
  withCredentials: true, // Required for JSESSIONID cookies
});

// Store session ID from login response
let sessionId = localStorage.getItem('sessionId') || null;

// Response interceptor to capture sessionId from login
api.interceptors.response.use(
  response => {
    if (response.data.sessionId) {
      sessionId = response.data.sessionId;
      localStorage.setItem('sessionId', sessionId);
      console.log('Session ID stored:', sessionId);
    }
    return response;
  },
  error => Promise.reject(error)
);

// Request interceptor to add sessionId as header if available
api.interceptors.request.use(
  config => {
    if (sessionId) {
      config.headers['X-JSESSIONID'] = sessionId;
    }
    return config;
  },
  error => Promise.reject(error)
);

export const authService = {
  login: (credentials) => api.post('/api/public/login', credentials, {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  }),
  signup: (userData) => api.post('/api/public/signup', userData),
  logout: () => api.post('/api/public/logout'),
};

export const studentService = {
  getAll: () => api.get('/api/students'),
  getById: (id) => api.get(`/api/students/${id}`),
  create: (data) => api.post('/api/students', data),
  update: (id, data) => api.put(`/api/students/${id}`, data),
  delete: (id) => api.delete(`/api/students/${id}`),
};

export default api;