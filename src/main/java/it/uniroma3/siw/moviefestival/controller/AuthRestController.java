package it.uniroma3.siw.moviefestival.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.uniroma3.siw.moviefestival.authentication.JwtUtils;
import it.uniroma3.siw.moviefestival.dto.LoginRequestDTO;
import it.uniroma3.siw.moviefestival.dto.LoginResponseDTO;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.service.UtenteService;

/**
 * Login stateless per il frontend React: verifica le credenziali con lo
 * stesso AuthenticationManager usato dal form login di Thymeleaf, e in
 * caso di successo restituisce un JWT invece di creare una sessione HTTP.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UtenteService utenteService;
    private final JwtUtils jwtUtils;

    public AuthRestController(AuthenticationManager authenticationManager,
                               UtenteService utenteService,
                               JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.utenteService = utenteService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO credenziali) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credenziali.getUsername(), credenziali.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("errore", "Username o password non validi"));
        }

        Utente utente = utenteService.findByUsername(credenziali.getUsername());
        String token = jwtUtils.generateToken(utente.getUsername(), utente.getRuolo().name());

        return ResponseEntity.ok(new LoginResponseDTO(token, utente.getUsername(), utente.getRuolo().name()));
    }
}
