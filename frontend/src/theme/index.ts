import { createTheme } from '@mui/material/styles'

const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: { main: '#e50914' },
    background: { default: '#141414', paper: '#1f1f1f' },
  },
  shape: { borderRadius: 8 },
})

export default theme
