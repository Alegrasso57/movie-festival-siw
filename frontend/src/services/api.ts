import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
})

// Ad ogni richiesta, se esiste un JWT salvato dal login, lo allega
// come header Authorization. Il backend lo legge in JwtAuthenticationFilter.
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
