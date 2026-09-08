import { useState } from 'react'
import type { FormEvent } from 'react'
import { Container, Paper, TextField, Button, Typography, Alert, Box } from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [errore, setErrore] = useState<string | null>(null)
  const [invio, setInvio] = useState(false)

  async function handleSubmit(evento: FormEvent) {
    evento.preventDefault()
    setErrore(null)
    setInvio(true)
    try {
      await login(username, password)
      navigate('/')
    } catch {
      setErrore('Username o password non validi')
    } finally {
      setInvio(false)
    }
  }

  return (
    <Container maxWidth="xs" sx={{ mt: 8 }}>
      <Paper sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom>Accedi</Typography>
        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 2 }}
        >
          {errore && <Alert severity="error">{errore}</Alert>}
          <TextField
            label="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            autoFocus
          />
          <TextField
            label="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button type="submit" variant="contained" disabled={invio}>
            Accedi
          </Button>
        </Box>
      </Paper>
    </Container>
  )
}
