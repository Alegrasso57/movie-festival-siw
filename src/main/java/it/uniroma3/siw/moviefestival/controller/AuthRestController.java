package it.uniroma3.siw.moviefestival.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.uniroma3.siw.moviefestival.dto.SessionInfoDTO;

/**
 * Il frontend React non gestisce un proprio login: l'autenticazione avviene
 * sempre sulla pagina /login del sito (form Thymeleaf classico), che crea
 * la normale sessione HTTP di Spring Security. Quella sessione è condivisa
 * anche dalle chiamate a /api/** (vedi SecurityConfig, un'unica catena di
 * sicurezza per tutta l'applicazione), quindi a React basta chiedere "chi
 * sono?" per sapere se mostrare le funzionalità riservate agli ADMIN
 * (ad es. la creazione di un film).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @GetMapping("/me")
    public SessionInfoDTO sessioneCorrente() {
        Authentication autenticazione = SecurityContextHolder.getContext().getAuthentication();

        boolean autenticato = autenticazione != null
                && autenticazione.isAuthenticated()
                && !"anonymousUser".equals(autenticazione.getPrincipal());

        if (!autenticato) {
            return new SessionInfoDTO(false, null, null);
        }

        String ruolo = autenticazione.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);

        return new SessionInfoDTO(true, autenticazione.getName(), ruolo);
    }
}
