package it.uniroma3.siw.moviefestival.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Lo username è obbligatorio")
    @Size(min = 3, message = "Lo username deve avere almeno 3 caratteri")
    private String username;

    // Contiene l'hash bcrypt, non la password in chiaro: la lunghezza
    // minima sulla password in chiaro viene controllata a monte in
    // UtenteService.registra(), prima della cifratura.
    @NotBlank(message = "La password è obbligatoria")
    private String password;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    // Costruttore vuoto, richiesto da JPA/Hibernate
    public Utente() {
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}