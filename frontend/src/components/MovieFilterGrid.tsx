import { useEffect, useMemo, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  CircularProgress,
  Alert,
} from '@mui/material'
import { getMovies } from '../services/movieService'
import type { Film } from '../types'

interface MovieFilterGridProps {
  // Quando MovieCreateDialog crea un nuovo film, viene passato qui e
  // preposto alla lista senza bisogno di richiamare il server (GET /movies
  // resta una chiamata sola, fatta al mount).
  newMovie?: Film | null
  // Valore iniziale della ricerca per titolo, es. da ?q=... nell'URL
  // (arriva dalla barra di ricerca nella nav del sito Thymeleaf).
  initialQuery?: string
}

// Pagina di ricerca film: volutamente semplice. Chi arriva qui l'ha fatto
// tramite la barra di ricerca del sito e vuole vedere solo il risultato
// della propria ricerca, non un intero catalogo con filtri avanzati
// (registi, intervallo anni, ordinamento) che qui non hanno senso.
export default function MovieFilterGrid({ newMovie, initialQuery }: MovieFilterGridProps) {
  const [allMovies, setAllMovies] = useState<Film[]>([])
  const [caricamento, setCaricamento] = useState(true)
  const [errore, setErrore] = useState<string | null>(null)

  const [testoRicerca, setTestoRicerca] = useState(initialQuery ?? '')

  useEffect(() => {
    getMovies()
      .then(setAllMovies)
      .catch(() => setErrore('Impossibile caricare i film dal server'))
      .finally(() => setCaricamento(false))
  }, [])

  useEffect(() => {
    if (newMovie) {
      setAllMovies((prev) => [newMovie, ...prev])
    }
  }, [newMovie])

  const filteredMovies = useMemo(() => {
    const query = testoRicerca.trim().toLowerCase()
    const risultato = query
      ? allMovies.filter((m) => m.titolo.toLowerCase().includes(query))
      : [...allMovies]
    return risultato.sort((a, b) => a.titolo.localeCompare(b.titolo))
  }, [allMovies, testoRicerca])

  if (caricamento) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress color="primary" />
      </Box>
    )
  }

  if (errore) {
    return <Alert severity="error" sx={{ mt: 2 }}>{errore}</Alert>
  }

  return (
    <Box>
      <TextField
        label="Cerca per titolo"
        placeholder="Es. Inception"
        value={testoRicerca}
        onChange={(e) => setTestoRicerca(e.target.value)}
        size="small"
        fullWidth
        sx={{ mb: 3, maxWidth: 360 }}
      />

      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
        {testoRicerca.trim()
          ? `${filteredMovies.length} film trovati per «${testoRicerca.trim()}»`
          : `${filteredMovies.length} film disponibili`}
      </Typography>

      {filteredMovies.length === 0 && (
        <Alert severity="info" sx={{ maxWidth: 480 }}>
          Nessun film trovato con questo titolo.
        </Alert>
      )}

      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
        {filteredMovies.map((film) => (
          <Card key={film.id} variant="outlined" sx={{ width: 280 }}>
            <CardContent>
              <Typography variant="h6">{film.titolo}</Typography>
              <Typography variant="body2" color="text.secondary">
                {film.anno} · {film.genere} · {film.durata} min
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {film.paeseProduzione}
              </Typography>
              {film.registaNome && (
                <Typography variant="body2" sx={{ mt: 1 }}>
                  Regia di {film.registaNome}
                </Typography>
              )}
            </CardContent>
          </Card>
        ))}
      </Box>
    </Box>
  )
}
