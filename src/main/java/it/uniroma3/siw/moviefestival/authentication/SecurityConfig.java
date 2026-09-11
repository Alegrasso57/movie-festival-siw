package it.uniroma3.siw.moviefestival.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Un'unica catena di sicurezza per tutta l'applicazione. Le pagine Thymeleaf
 * e le API REST usate dal frontend React (sotto /api/**) condividono la
 * stessa sessione HTTP creata dal login: il frontend React non fa un login
 * separato, si affida al cookie di sessione che il browser invia
 * automaticamente anche alle chiamate a /api/** quando l'utente si è già
 * autenticato tramite la normale pagina /login del sito.
 *
 * Le richieste a /api/** sono esentate dal controllo CSRF perché il
 * frontend React non genera il token nascosto che Thymeleaf inserisce
 * automaticamente nelle form (vedi thymeleaf-extras-springsecurity6); lo
 * stesso vale per /logout, così un semplice form HTML dentro l'app React
 * può disconnettere l'utente senza dover gestire quel token.
 *
 * Oltre al login classico (form + tabella Utente) è abilitato anche il
 * login con Google (OAuth2/OIDC, vedi CustomOidcUserService): entrambi
 * portano alla stessa sessione autenticata, quindi tutto il resto
 * dell'applicazione (autorizzazioni, profilo, recensioni...) non deve
 * distinguere in alcun modo com'è avvenuto l'accesso.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final CustomOidcUserService customOidcUserService;

    public SecurityConfig(UserDetailsService userDetailsService, CustomOidcUserService customOidcUserService) {
        this.userDetailsService = userDetailsService;
        this.customOidcUserService = customOidcUserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/logout"))

            .authorizeHttpRequests(authorize -> authorize

                .requestMatchers(HttpMethod.GET,
                        "/", "/index",
                        "/css/**", "/js/**", "/images/**", "/webjars/**",
                        "/app", "/app/**",
                        "/festivals", "/festival/**",
                        "/movies", "/movie/**",
                        "/screenings",
                        "/register", "/login", "/error",
                        "/oauth2/**", "/login/oauth2/**")
                .permitAll()

                .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/auth/me", "/api/festivals/**", "/api/movies/**", "/api/registi")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/movies").hasAuthority("ADMIN")

                .requestMatchers("/admin/**").hasAuthority("ADMIN")

                .anyRequest().authenticated())

            .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error=true")
                    .permitAll())

            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOidcUserService))
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error=true")
                    .permitAll())

            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll());

        return http.build();
    }
}
