package it.uniroma3.siw.moviefestival.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Genera e valida i JWT usati dal frontend React per autenticarsi verso le
 * API REST (/api/**), che sono stateless. L'autenticazione a sessione con
 * Spring Security (form login) resta invariata per le pagine Thymeleaf.
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, String ruolo) {
        Date adesso = new Date();
        Date scadenza = new Date(adesso.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .claim("ruolo", ruolo)
                .issuedAt(adesso)
                .expiration(scadenza)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValido(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRuolo(String token) {
        return parseClaims(token).get("ruolo", String.class);
    }
}
