package it.uniroma3.siw.moviefestival.authentication;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Due catene di sicurezza distinte:
 *  - apiFilterChain (/api/**): pensata per il frontend React, autenticazione via JWT
 *    (header Authorization). Riconosce comunque la sessione Thymeleaf se già presente,
 *    per non rompere il widget di recensioni esistente in movieDetail.html.
 *  - webFilterChain (tutto il resto): sessione HTTP con form login, usata dalle pagine Thymeleaf.
 * Sono indipendenti: una richiesta a /api/** non passa mai per la seconda catena e viceversa.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                           JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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

    /**
     * CORS per il frontend React (Vite gira su http://localhost:5173 in sviluppo).
     * Il JWT viaggia nell'header Authorization: non serve allowCredentials/cookie cross-site.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http,
                                               CorsConfigurationSource corsConfigurationSource) throws Exception {

        http
            .securityMatcher("/api/**")
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable())
            // IF_REQUIRED (non STATELESS): il widget di recensioni già presente in
            // movieDetail.html chiama /api/** riusando la sessione del login Thymeleaf.
            // Lasciando IF_REQUIRED, Spring Security continua a riconoscere quella
            // sessione se presente; il nuovo frontend React invece non ne crea mai una
            // e si autentica solo tramite JwtAuthenticationFilter (token in Authorization).
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/festivals/**", "/api/movies/**", "/api/registi").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/movies").hasAuthority("ADMIN")
                .anyRequest().authenticated())

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(authorize -> authorize

                .requestMatchers(HttpMethod.GET,
                        "/", "/index",
                        "/css/**", "/js/**", "/images/**", "/webjars/**",
                        "/festivals", "/festival/**",
                        "/movies", "/movie/**",
                        "/screenings",
                        "/register", "/login", "/error")
                .permitAll()

                .requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()

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

            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll());

        return http.build();
    }
}
