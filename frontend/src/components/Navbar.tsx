import { AppBar, Toolbar, Typography, Button, Box, Link as MuiLink } from '@mui/material'
import { useAuth } from '../context/AuthContext'
import { VINO, ORO } from '../theme'

export default function Navbar() {
  const { user, isAuthenticated, caricamento } = useAuth()

  return (
    // Stessa barra "vino" con costa oro della nav del sito Thymeleaf (vedi
    // style.css, nav.main-nav), così questa pagina di ricerca non sembra
    // un'app a sé ma una parte dello stesso sito.
    <AppBar
      position="static"
      elevation={0}
      sx={{ backgroundColor: VINO, borderBottom: `0.2rem solid ${ORO}` }}
    >
      <Toolbar variant="dense" sx={{ gap: 2 }}>
        <MuiLink
          href="/"
          underline="none"
          color="inherit"
          sx={{ fontSize: '0.85em', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.06em' }}
        >
          ← Torna al sito
        </MuiLink>
        <Typography variant="subtitle1" sx={{ flexGrow: 1, fontFamily: "Georgia, 'Times New Roman', serif" }}>
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
                <Button
                  color="inherit"
                  size="small"
                  type="submit"
                  sx={{ textTransform: 'uppercase', fontWeight: 600, letterSpacing: '0.04em' }}
                >
                  Esci
                </Button>
              </Box>
            </>
          ) : (
            !caricamento && (
              <Button
                color="inherit"
                size="small"
                href="/login"
                sx={{ textTransform: 'uppercase', fontWeight: 600, letterSpacing: '0.04em' }}
              >
                Accedi
              </Button>
            )
          )}
        </Box>
      </Toolbar>
    </AppBar>
  )
}
