import api from './api'
import type { SessionInfo } from '../types'

// Non esiste un login separato per React: l'utente si autentica sulla
// pagina /login del sito (form Thymeleaf). Questa chiamata chiede solo
// "chi sono?" secondo la sessione HTTP che il browser invia comunque
// insieme a ogni richiesta verso /api/** (vedi services/api.ts).
export async function getSessionInfo(): Promise<SessionInfo> {
  const { data } = await api.get<SessionInfo>('/auth/me')
  return data
}
