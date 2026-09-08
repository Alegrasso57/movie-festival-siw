import { createTheme } from '@mui/material/styles'

// Stessa palette "gala" del sito Thymeleaf (vedi style.css, sezione
// "Variabili CSS": --rosa, --vino, --oro...), così questa pagina React di
// ricerca risulta coerente con il resto del sito invece di avere un tema
// a parte. Le costanti sono esportate così Navbar.tsx può riusarle senza
// duplicare i valori esadecimali.
export const VINO = '#6e3f52'
export const VINO_SCURO = '#5c3444'
export const ORO = '#c9a15a'
export const ORO_SCURO = '#ad873f'
export const ROSA = '#b5677a'
export const ROSA_SCURO = '#96495c'
export const BG_COLOR = '#fbf3ee'
export const CARD_BG = '#fffdfb'
export const TEXT_COLOR = '#3d2c34'
export const MUTED_COLOR = '#9c7c87'
export const BORDER_COLOR = '#f0dce0'

const SERIF = "Georgia, 'Times New Roman', serif"

const theme = createTheme({
  palette: {
    mode: 'light',
    // Rosa: colore dei pulsanti "azione" (come button{} nel sito Thymeleaf)
    primary: { main: ROSA, dark: ROSA_SCURO, contrastText: '#ffffff' },
    // Oro: colore d'accento (come --oro nel sito Thymeleaf)
    secondary: { main: ORO, dark: ORO_SCURO, contrastText: VINO_SCURO },
    background: { default: BG_COLOR, paper: CARD_BG },
    text: { primary: TEXT_COLOR, secondary: MUTED_COLOR },
  },
  shape: { borderRadius: 12 },
  typography: {
    fontFamily: "'Segoe UI', Arial, sans-serif",
    h1: { fontFamily: SERIF, color: VINO },
    h2: { fontFamily: SERIF, color: VINO },
    h3: { fontFamily: SERIF, color: VINO },
    h4: { fontFamily: SERIF, color: VINO },
    h5: { fontFamily: SERIF, color: VINO },
    h6: { fontFamily: SERIF, color: VINO, fontWeight: 700 },
  },
  components: {
    MuiCard: {
      styleOverrides: {
        root: {
          // Stessa "costa" dorata sul lato sinistro delle card del sito
          // Thymeleaf (vedi style.css, .card), non più un bordo semplice
          borderColor: BORDER_COLOR,
          borderLeft: `0.1875rem solid ${ORO}`,
          borderRadius: '0 0.75rem 0.75rem 0',
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          // Forma a pillola, stessa firma ricorrente del sito Thymeleaf
          // (vedi style.css, button{})
          borderRadius: 32,
          textTransform: 'none',
          fontWeight: 600,
        },
      },
    },
  },
})

export default theme
