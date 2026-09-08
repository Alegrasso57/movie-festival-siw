import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, isAuthenticated, logout } = useAuth()
  const navigate = useNavigate()

  function handleLogout() {
    logout()
    navigate('/')
  }

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          SIW Movie Festival
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {isAuthenticated ? (
            <>
              <Typography variant="body2">
                {user?.username} ({user?.ruolo})
              </Typography>
              <Button color="inherit" onClick={handleLogout}>Esci</Button>
            </>
          ) : (
            <Button color="inherit" onClick={() => navigate('/login')}>Accedi</Button>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  )
}
