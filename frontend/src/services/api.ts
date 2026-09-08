import axios from 'axios'

// Percorso relativo: funziona sia in produzione (l'app React è servita da
// Spring Boot sotto /app, quindi /api è la stessa origine) sia in sviluppo
// (il proxy di Vite in vite.config.ts inoltra /api al backend su :8080).
//
// withCredentials: true fa sì che il cookie di sessione (JSESSIONID),
// creato dal login sulla pagina Thymeleaf del sito, venga inviato anche
// dalle chiamate axios: non serve un token separato per React, l'app
// riusa la stessa sessione di autenticazione di tutto il resto del sito.
const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
})

export default api
