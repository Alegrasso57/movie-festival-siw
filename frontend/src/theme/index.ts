import { createTheme } from '@mui/material/styles'

// Stessa palette pastello del sito Thymeleaf (vedi style.css, sezione
// "Variabili CSS"), così la pagina React di ricerca risulta coerente con
// il resto del sito invece di avere un tema scuro a parte.
const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#c17685', dark: '#a85f6d', contrastText: '#ffffff' },
    secondary: { main: '#6f9779', dark: '#5c7f65', contrastText: '#ffffff' },
    background: { default: '#faf6f2', paper: '#ffffff' },
    text: { primary: '#3a3540', secondary: '#8a7d94' },
  },
  shape: { borderRadius: 12 },
  typography: {
    fontFamily: "'Segoe UI', Arial, sans-serif",
  },
  components: {
    MuiCard: {
      styleOverrides: {
        root: {
          borderColor: '#e5dfe8',
        },
      },
    },
  },
})

export default theme
