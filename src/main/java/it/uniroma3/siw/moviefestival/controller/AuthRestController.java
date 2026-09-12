package it.uniroma3.siw.moviefestival.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.moviefestival.dto.SessionInfoDTO;


@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @GetMapping("/me")
    public SessionInfoDTO sessioneCorrente() {
        Authentication autenticazione = SecurityContextHolder.getContext().getAuthentication();

        if (autenticazione == null
                || !autenticazione.isAuthenticated()
                || "anonymousUser".equals(autenticazione.getPrincipal())) {
            return new SessionInfoDTO(false, null, null);
        }

        String ruolo = autenticazione.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);

        return new SessionInfoDTO(true, autenticazione.getName(), ruolo);
    }
}
