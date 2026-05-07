import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // Required for JSESSIONID cookies
});

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