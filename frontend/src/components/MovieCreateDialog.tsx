import { useState } from 'react'
import type { ChangeEvent } from 'react'
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Alert,
  CircularProgress,
} from '@mui/material'
import type { SelectChangeEvent } from '@mui/material'
import { createMovie } from '../services/movieService'
import type { Film, NuovoFilm, Regista } from '../types'

interface MovieCreateDialogProps {
  registi: Regista[]
  onCreato: (film: Film) => void
}

const valoriIniziali: NuovoFilm = {
  titolo: '',
  anno: new Date().getFullYear(),
  durata: 90,
  genere: '',
  paeseProduzione: '',
  registaId: 0,
}

export default function MovieCreateDialog({ registi, onCreato }: MovieCreateDialogProps) {
  const [aperto, setAperto] = useState(false)
  const [valori, setValori] = useState<NuovoFilm>(valoriIniziali)
  const [errore, setErrore] = useState<string | null>(null)
  const [invio, setInvio] = useState(false)

  function apri() {
    setValori(valoriIniziali)
    setErrore(null)
    setAperto(true)
  }

  function chiudi() {
    if (!invio) setAperto(false)
  }

  // Un handler per campo, invece di un unico handler generico con chiave
  // dinamica: più verboso, ma type-safe senza cast in modalità strict.
  function handleTitoloChange(e: ChangeEvent<HTMLInputElement>) {
    setValori((prev) => ({ ...prev, titolo: e.target.value }))
  }
  function handleAnnoChange(e: ChangeEvent<HTMLInputElement>) {
    setValori((prev) => ({ ...prev, anno: Number(e.target.value) }))
  }
  function handleDurataChange(e: ChangeEvent<HTMLInputElement>) {
    setValori((prev) => ({ ...prev, durata: Number(e.target.value) }))
  }
  function handleGenereChange(e: ChangeEvent<HTMLInputElement>) {
    setValori((prev) => ({ ...prev, genere: e.target.value }))
  }
  function handlePaeseChange(e: ChangeEvent<HTMLInputElement>) {
    setValori((prev) => ({ ...prev, paeseProduzione: e.target.value }))
  }
  function handleRegistaChange(evento: SelectChangeEvent<number>) {
    setValori((prev) => ({ ...prev, registaId: Number(evento.target.value) }))
  }

  async function handleSubmit() {
    setErrore(null)

    if (!valori.titolo.trim()) {
      setErrore('Il titolo è obbligatorio')
      return
    }
    if (!valori.registaId) {
      setErrore('Seleziona un regista')
      return
    }

    setInvio(true)
    try {
      const nuovoFilm = await createMovie(valori)
      onCreato(nuovoFilm)
      setAperto(false)
    } catch (err) {
      const risposta = (err as { response?: { data?: { errore?: string } } }).response
      setErrore(risposta?.data?.errore ?? 'Errore durante la creazione del film')
    } finally {
      setInvio(false)
    }
  }

  return (
    <>
      <Button variant="contained" onClick={apri}>
        Nuovo film
      </Button>

      <Dialog open={aperto} onClose={chiudi} fullWidth maxWidth="sm">
        <DialogTitle>Aggiungi un nuovo film</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
          {errore && <Alert severity="error">{errore}</Alert>}

          <TextField label="Titolo" value={valori.titolo} onChange={handleTitoloChange} fullWidth />
          <TextField label="Anno" type="number" value={valori.anno} onChange={handleAnnoChange} fullWidth />
          <TextField label="Durata (minuti)" type="number" value={valori.durata} onChange={handleDurataChange} fullWidth />
          <TextField label="Genere" value={valori.genere} onChange={handleGenereChange} fullWidth />
          <TextField label="Paese di produzione" value={valori.paeseProduzione} onChange={handlePaeseChange} fullWidth />

          <FormControl fullWidth>
            <InputLabel id="regista-label">Regista</InputLabel>
            <Select
              labelId="regista-label"
              label="Regista"
              value={valori.registaId || ''}
              onChange={handleRegistaChange}
            >
              {registi.map((r) => (
                <MenuItem key={r.id} value={r.id}>
                  {r.nome} {r.cognome}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={chiudi} disabled={invio}>Annulla</Button>
          <Button variant="contained" onClick={handleSubmit} disabled={invio}>
            {invio ? <CircularProgress size={20} /> : 'Crea film'}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  )
}
