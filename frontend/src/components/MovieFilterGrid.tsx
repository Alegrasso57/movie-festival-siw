import { useEffect, useMemo, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  Chip,
  Slider,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  CircularProgress,
  Alert,
} from '@mui/material'
import type { SelectChangeEvent } from '@mui/material'
import { getMovies } from '../services/movieService'
import type { Film } from '../types'

type Ordinamento = 'titoloAsc' | 'titoloDesc' | 'annoAsc' | 'annoDesc'

interface MovieFilterGridProps {
  // Quando MovieCreateDialog crea un nuovo film, viene passato qui e
  // preposto alla lista senza bisogno di richiamare il server (GET /movies
  // resta una chiamata sola, fatta al mount).
  newMovie?: Film | null
}

export default function MovieFilterGrid({ newMovie }: MovieFilterGridProps) {
  const [allMovies, setAllMovies] = useState<Film[]>([])
  const [caricamento, setCaricamento] = useState(true)
  const [errore, setErrore] = useState<string | null>(null)

  const [registaSelezionato, setRegistaSelezionato] = useState<string | null>(null)
  const [annoRange, setAnnoRange] = useState<[number, number] | null>(null)
  const [ordinamento, setOrdinamento] = useState<Ordinamento>('titoloAsc')

  useEffect(() => {
    getMovies()
      .then((movies) => {
        setAllMovies(movies)
        if (movies.length > 0) {
          const anni = movies.map((m) => m.anno)
          setAnnoRange([Math.min(...anni), Math.max(...anni)])
        }
      })
      .catch(() => setErrore('Impossibile caricare i film dal server'))
      .finally(() => setCaricamento(false))
  }, [])

  useEffect(() => {
    if (newMovie) {
      setAllMovies((prev) => [newMovie, ...prev])
    }
  }, [newMovie])

  const registiDisponibili = useMemo(() => {
    const nomi = allMovies
      .map((m) => m.registaNome)
      .filter((nome): nome is string => Boolean(nome))
    return Array.from(new Set(nomi)).sort()
  }, [allMovies])

  const limitiAnno = useMemo((): [number, number] => {
    if (allMovies.length === 0) return [1888, new Date().getFullYear()]
    const anni = allMovies.map((m) => m.anno)
    return [Math.min(...anni), Math.max(...anni)]
  }, [allMovies])

  const filteredMovies = useMemo(() => {
    let risultato = [...allMovies]

    if (registaSelezionato) {
      risultato = risultato.filter((m) => m.registaNome === registaSelezionato)
    }

    if (annoRange) {
      risultato = risultato.filter((m) => m.anno >= annoRange[0] && m.anno <= annoRange[1])
    }

    switch (ordinamento) {
      case 'titoloAsc':
        risultato.sort((a, b) => a.titolo.localeCompare(b.titolo))
        break
      case 'titoloDesc':
        risultato.sort((a, b) => b.titolo.localeCompare(a.titolo))
        break
      case 'annoAsc':
        risultato.sort((a, b) => a.anno - b.anno)
        break
      case 'annoDesc':
        risultato.sort((a, b) => b.anno - a.anno)
        break
    }

    return risultato
  }, [allMovies, registaSelezionato, annoRange, ordinamento])

  function toggleRegista(nome: string) {
    // Chip esclusivi: cliccarne uno già attivo lo deseleziona tornando a "Tutti".
    setRegistaSelezionato((attuale) => (attuale === nome ? null : nome))
  }

  function handleOrdinamentoChange(evento: SelectChangeEvent) {
    setOrdinamento(evento.target.value as Ordinamento)
  }

  if (caricamento) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    )
  }

  if (errore) {
    return <Alert severity="error" sx={{ mt: 2 }}>{errore}</Alert>
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mb: 3 }}>
        <Chip
          label="Tutti i registi"
          color={registaSelezionato === null ? 'primary' : 'default'}
          onClick={() => setRegistaSelezionato(null)}
        />
        {registiDisponibili.map((nome) => (
          <Chip
            key={nome}
            label={nome}
            color={registaSelezionato === nome ? 'primary' : 'default'}
            onClick={() => toggleRegista(nome)}
          />
        ))}
      </Box>

      <Box sx={{ maxWidth: 400, mb: 3 }}>
        <Typography gutterBottom>
          Anno: {annoRange ? `${annoRange[0]} — ${annoRange[1]}` : ''}
        </Typography>
        <Slider
          value={annoRange ?? limitiAnno}
          min={limitiAnno[0]}
          max={limitiAnno[1]}
          onChange={(_, valore) => setAnnoRange(valore as [number, number])}
          valueLabelDisplay="auto"
        />
      </Box>

      <FormControl sx={{ minWidth: 220, mb: 3 }} size="small">
        <InputLabel id="ordinamento-label">Ordina per</InputLabel>
        <Select
          labelId="ordinamento-label"
          value={ordinamento}
          label="Ordina per"
          onChange={handleOrdinamentoChange}
        >
          <MenuItem value="titoloAsc">Titolo A → Z</MenuItem>
          <MenuItem value="titoloDesc">Titolo Z → A</MenuItem>
          <MenuItem value="annoAsc">Anno crescente</MenuItem>
          <MenuItem value="annoDesc">Anno decrescente</MenuItem>
        </Select>
      </FormControl>

      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
        {filteredMovies.length} film trovati
      </Typography>

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
