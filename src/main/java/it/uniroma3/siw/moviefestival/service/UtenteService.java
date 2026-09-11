package it.uniroma3.siw.moviefestival.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Ruolo;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.repository.UtenteRepository;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional(readOnly = true)
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public Utente findById(Long id) {
        return utenteRepository.findById(id).orElse(null);
    }

    /**
     * Registra un nuovo utente con ruolo USER, cifrando la password.
     * Verifica che lo username non sia già in uso.
     */
    @Transactional
    public Utente registra(String username, String passwordInChiaro) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Lo username è obbligatorio");
        }
        if (username.trim().length() < 3) {
            throw new IllegalArgumentException("Lo username deve avere almeno 3 caratteri");
        }
        if (passwordInChiaro == null || passwordInChiaro.isBlank()) {
            throw new IllegalArgumentException("La password è obbligatoria");
        }
        if (passwordInChiaro.length() < 6) {
            throw new IllegalArgumentException("La password deve avere almeno 6 caratteri");
        }

        Utente esistente = utenteRepository.findByUsername(username).orElse(null);
        if (esistente != null) {
            throw new IllegalStateException("Username già in uso");
        }

        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setPassword(passwordEncoder.encode(passwordInChiaro));
        utente.setRuolo(Ruolo.USER);

        return utenteRepository.save(utente);
    }

    /**
     * Aggiorna username e (facoltativamente) password dell'utente indicato.
     * Se nuovaPasswordInChiaro e' vuota o nulla la password resta invariata:
     * questo permette il semplice cambio username dal proprio profilo.
     */
    @Transactional
    public Utente aggiornaProfilo(Long id, String nuovoUsername, String nuovaPasswordInChiaro) {

        Utente utente = utenteRepository.findById(id).orElse(null);
        if (utente == null) {
            throw new IllegalStateException("Utente non trovato");
        }

        if (nuovoUsername == null || nuovoUsername.isBlank()) {
            throw new IllegalArgumentException("Lo username è obbligatorio");
        }
        if (nuovoUsername.trim().length() < 3) {
            throw new IllegalArgumentException("Lo username deve avere almeno 3 caratteri");
        }

        Utente altro = utenteRepository.findByUsername(nuovoUsername).orElse(null);
        if (altro != null && !altro.getId().equals(id)) {
            throw new IllegalStateException("Username già in uso");
        }

        utente.setUsername(nuovoUsername);

        if (nuovaPasswordInChiaro != null && !nuovaPasswordInChiaro.isBlank()) {
            if (nuovaPasswordInChiaro.length() < 6) {
                throw new IllegalArgumentException("La password deve avere almeno 6 caratteri");
            }
            utente.setPassword(passwordEncoder.encode(nuovaPasswordInChiaro));
        }

        return utenteRepository.save(utente);
    }

    /**
     * Collega il login con Google a un Utente della nostra tabella: se un
     * utente con questa email (usata come username) non esiste ancora, lo
     * crea con ruolo USER. La password è casuale e cifrata: chi entra con
     * Google non la userà mai (il campo esiste solo perché non è nullable),
     * quindi non c'è nessun problema di sicurezza nel generarla qui.
     */
    @Transactional
    public Utente trovaOCreaUtenteGoogle(String email) {

        Utente esistente = utenteRepository.findByUsername(email).orElse(null);
        if (esistente != null) {
            return esistente;
        }

        Utente utente = new Utente();
        utente.setUsername(email);
        utente.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
        utente.setRuolo(Ruolo.USER);

        return utenteRepository.save(utente);
    }

    /**
     * Elimina definitivamente l'account con l'id indicato.
     */
    @Transactional
    public void elimina(Long id) {
        utenteRepository.deleteById(id);
    }
}