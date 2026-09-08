package it.uniroma3.siw.moviefestival.dto;

/**
 * Risposta di GET /api/auth/me: dice al frontend React chi è l'utente
 * corrente secondo la sessione HTTP (la stessa creata dal login Thymeleaf),
 * senza bisogno di un token separato.
 */
public class SessionInfoDTO {

    private boolean autenticato;
    private String username;
    private String ruolo;

    public SessionInfoDTO() {
    }

    public SessionInfoDTO(boolean autenticato, String username, String ruolo) {
        this.autenticato = autenticato;
        this.username = username;
        this.ruolo = ruolo;
    }

    public boolean isAutenticato() {
        return autenticato;
    }

    public void setAutenticato(boolean autenticato) {
        this.autenticato = autenticato;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
