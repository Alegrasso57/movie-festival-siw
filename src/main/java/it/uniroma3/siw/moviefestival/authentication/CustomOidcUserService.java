package it.uniroma3.siw.moviefestival.authentication;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.service.UtenteService;

/**
 * Dopo che Google ha confermato l'identità della persona, questo servizio la
 * collega a un Utente della nostra tabella (usando l'email come username),
 * creandolo al volo se è il primo accesso. In questo modo il resto
 * dell'applicazione — che lavora sempre con Authentication.getName() e la
 * tabella Utente per ricavare ruolo e profilo — funziona identico sia per
 * chi accede con la form classica sia per chi entra con Google.
 */
@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService delegate = new OidcUserService();
    private final UtenteService utenteService;

    public CustomOidcUserService(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser oidcUser = delegate.loadUser(userRequest);

        String email = oidcUser.getEmail();
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("L'account Google non espone un indirizzo email");
        }

        Utente utente = utenteService.trovaOCreaUtenteGoogle(email);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(utente.getRuolo().name()));

        // "email" come nameAttributeKey: così Authentication.getName() (usato
        // in tutta l'app, es. ProfiloController, RecensioneController) torna
        // l'indirizzo email, che è anche lo username salvato nella tabella
        // Utente — esattamente come per il login classico.
        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }
}
