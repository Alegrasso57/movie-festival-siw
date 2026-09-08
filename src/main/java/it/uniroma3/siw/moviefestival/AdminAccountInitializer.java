package it.uniroma3.siw.moviefestival;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import it.uniroma3.siw.moviefestival.model.Ruolo;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.repository.UtenteRepository;

/**
 * All'avvio dell'applicazione, garantisce che esista almeno un utente con
 * ruolo ADMIN — necessario perché la registrazione pubblica (vedi
 * UtenteService.registra) crea sempre utenti USER, quindi senza questo
 * bootstrap non ci sarebbe alcun modo di accedere alle funzionalità di
 * amministrazione (creazione di festival, film, registi, sale, proiezioni),
 * che vanno inserite a mano dal pannello /admin.
 *
 * Non inserisce nessun altro dato di prova: festival, film, registi e sale
 * li aggiunge direttamente l'amministratore dal sito.
 */
@Component
public class AdminAccountInitializer implements CommandLineRunner {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private final UtenteRepository utenteRepository;

    public AdminAccountInitializer(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Controllo esplicito sull'esistenza dell'utente, non sul conteggio
        // totale: evita di creare un secondo "admin" duplicato se in futuro
        // altri utenti USER vengono cancellati o il database cambia stato.
        if (utenteRepository.findByUsername(ADMIN_USERNAME).isPresent()) {
            return;
        }

        Utente admin = new Utente();
        admin.setUsername(ADMIN_USERNAME);
        admin.setPassword(new BCryptPasswordEncoder().encode(ADMIN_PASSWORD));
        admin.setRuolo(Ruolo.ADMIN);
        utenteRepository.save(admin);

        System.out.println(">>> Utente amministratore creato: " + ADMIN_USERNAME + " / " + ADMIN_PASSWORD);
    }
}
