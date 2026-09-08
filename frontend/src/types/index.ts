// Le interfacce qui sotto rispecchiano esattamente i DTO Java del backend
// (package it.uniroma3.siw.moviefestival.dto). Se il backend cambia,
// vanno aggiornate qui.

export interface Regista {
  id: number
  nome: string
  cognome: string
}

// Rispecchia FilmDTO.java
export interface Film {
  id: number
  titolo: string
  anno: number
  durata: number
  genere: string
  paeseProduzione: string
  registaNome?: string
}

// Corpo della richiesta di creazione — rispecchia FilmCreateDTO.java
export interface NuovoFilm {
  titolo: string
  anno: number
  durata: number
  genere: string
  paeseProduzione: string
  registaId: number
}

export type Ruolo = 'USER' | 'ADMIN'

// Rispecchia SessionInfoDTO.java — risposta di GET /api/auth/me
export interface SessionInfo {
  autenticato: boolean
  username: string | null
  ruolo: Ruolo | null
}
