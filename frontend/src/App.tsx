import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { ThemeProvider, CssBaseline } from '@mui/material'
import theme from './theme'
import { AuthProvider } from './context/AuthContext'
import Navbar from './components/Navbar'
import HomePage from './pages/HomePage'

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        {/* basename="/app": l'app React vive sotto /app quando è servita da
            Spring Boot (vedi WebConfig.java e vite.config.ts -> base).
            Non c'è una rotta /login: l'accesso avviene sulla pagina di
            login del sito (vedi Navbar), non dentro questa SPA. */}
        <BrowserRouter basename="/app">
          <Navbar />
          <Routes>
            <Route path="/" element={<HomePage />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </ThemeProvider>
  )
}
