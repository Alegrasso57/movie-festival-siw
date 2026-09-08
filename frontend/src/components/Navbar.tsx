import { AppBar, Toolbar, Typography, Button, Box, Link as MuiLink } from '@mui/material'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, isAuthenticated, caricamento } = useAuth()

  return (
    // Barra compatta in verde pastello: stessa tinta della nav del sito
    // Thymeleaf (vedi style.css, --nav-color), così questa pagina di
    // ricerca non sembra un'app a sé ma una parte dello stesso sito.
    <AppBar position="static" color="secondary" elevation={0}>
      <Toolbar variant="dense" sx={{ gap: 2 }}>
        <MuiLink href="/" underline="hover" color="inherit" sx={{ fontSize: '0.85em' }}>
          ← Torna al sito
        </MuiLink>
        <Typography variant="subtitle1" sx={{ flexGrow: 1 }}>
          Ricerca film
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {!caricamento && isAuthenticated ? (
            <>
              <Typography variant="body2">
                {user?.username} ({user?.ruolo})
              </Typography>
              {/* Logout: un vero form POST verso /logout, lo stesso
                  endpoint usato dal sito Thymeleaf (vedi index.html).
                  Niente chiamata axios: è una navigazione a tutti gli
                  effetti, coerente con come funziona il resto del sito. */}
              <Box component="form" action="/logout" method="post">
                <Button color="inherit" size="small" type="submit">Esci</Button>
              </Box>
            </>
          ) : (
            !caricamento && (
              <Button color="inherit" size="small" href="/login">Accedi</Button>
            )
          )}
        </Box>
      </Toolbar>
    </AppBar>
  )
}
