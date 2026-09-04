package it.uniroma3.siw.moviefestival.authentication;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.service.UtenteService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteService utenteService;

    public CustomUserDetailsService(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Utente utente = utenteService.findByUsername(username);
        if (utente == null) {
            throw new UsernameNotFoundException("Utente non trovato: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                utente.getUsername(),
                utente.getPassword(),
                List.of(new SimpleGrantedAuthority(utente.getRuolo().name()))
        );
    }
}
