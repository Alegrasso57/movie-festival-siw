import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import type { ReactNode } from 'react'
import { getSessionInfo } from '../services/authService'
import type { Ruolo } from '../types'

interface AuthUser {
  username: string
  ruolo: Ruolo
}

interface AuthContextValue {
  user: AuthUser | null
  isAuthenticated: boolean
  caricamento: boolean
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

// Niente login/logout qui dentro: l'autenticazione avviene sempre sulla
// pagina /login del sito (form Thymeleaf classico), che crea la sessione
// HTTP condivisa da tutta l'applicazione. Questo contesto si limita a
// chiedere al backend "chi sono?" (GET /api/auth/me) all'avvio, per sapere
// se mostrare le funzionalità riservate agli ADMIN (es. creare un film).
export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null)
  const [caricamento, setCaricamento] = useState(true)

  const caricaSessione = useCallback(() => {
    setCaricamento(true)
    getSessionInfo()
      .then((info) => {
        setUser(
          info.autenticato && info.username && info.ruolo
            ? { username: info.username, ruolo: info.ruolo }
            : null
        )
      })
      .catch(() => setUser(null))
      .finally(() => setCaricamento(false))
  }, [])

  useEffect(() => {
    caricaSessione()
  }, [caricaSessione])

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: user !== null, caricamento }}>
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
