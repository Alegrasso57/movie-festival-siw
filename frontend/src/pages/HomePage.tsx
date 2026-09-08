import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { Container, Typography, Box } from '@mui/material'
import MovieFilterGrid from '../components/MovieFilterGrid'
import MovieCreateDialog from '../components/MovieCreateDialog'
import { getRegisti } from '../services/registaService'
import { useAuth } from '../context/AuthContext'
import type { Film, Regista } from '../types'

export default function HomePage() {
  const { user } = useAuth()
  const [registi, setRegisti] = useState<Regista[]>([])
  const [ultimoFilmCreato, setUltimoFilmCreato] = useState<Film | null>(null)

  // La barra di ricerca del sito (nella nav Thymeleaf) invia qui con
  // /app?q=..., letto una volta al caricamento e passato come ricerca
  // iniziale a MovieFilterGrid.
  const [searchParams] = useSearchParams()
  const ricercaIniziale = searchParams.get('q') ?? ''

  useEffect(() => {
    if (user?.ruolo === 'ADMIN') {
      getRegisti().then(setRegisti).catch(() => setRegisti([]))
    }
  }, [user])

  return (
    <Container sx={{ py: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3, flexWrap: 'wrap', gap: 2 }}>
        <Typography variant="h4">
          {ricercaIniziale ? 'Risultati di ricerca' : 'Film del festival'}
        </Typography>
        {user?.ruolo === 'ADMIN' && (
          <MovieCreateDialog registi={registi} onCreato={setUltimoFilmCreato} />
        )}
      </Box>
      <MovieFilterGrid newMovie={ultimoFilmCreato} initialQuery={ricercaIniziale} />
    </Container>
  )
}
