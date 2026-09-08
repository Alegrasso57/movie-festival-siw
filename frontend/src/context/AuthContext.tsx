import { createContext, useContext, useState, useEffect } from 'react'
import type { ReactNode } from 'react'
import * as authService from '../services/authService'
import type { Ruolo } from '../types'

interface AuthUser {
  username: string
  ruolo: Ruolo
}

interface AuthContextValue {
  user: AuthUser | null
  isAuthenticated: boolean
  login: (username: string, password: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null)

  // Token recovery: all'avvio dell'app, se un utente aveva già fatto login,
  // ripristina lo stato da localStorage invece di forzare un nuovo login.
  useEffect(() => {
    const token = localStorage.getItem('token')
    const username = localStorage.getItem('username')
    const ruolo = localStorage.getItem('ruolo') as Ruolo | null
    if (token && username && ruolo) {
      setUser({ username, ruolo })
    }
  }, [])

  async function login(username: string, password: string) {
    const risposta = await authService.login(username, password)
    localStorage.setItem('token', risposta.token)
    localStorage.setItem('username', risposta.username)
    localStorage.setItem('ruolo', risposta.ruolo)
    setUser({ username: risposta.username, ruolo: risposta.ruolo })
  }

  function logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('ruolo')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: user !== null, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth deve essere usato dentro un AuthProvider')
  }
  return context
}
