package it.uniroma3.siw.moviefestival.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.service.UtenteService;

/**
 * Gestione del profilo dell'utente attualmente autenticato: permette di
 * cambiare username/password o di eliminare il proprio account. Tutte le
 * rotte sotto /profilo richiedono un utente autenticato (regola di default
 * "anyRequest().authenticated()" in SecurityConfig, nessuna modifica
 * necessaria li').
 */
@Controller
public class ProfiloController {

    private final UtenteService utenteService;

    public ProfiloController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/profilo/modifica")
    public String formModifica(Authentication authentication, Model model) {
        Utente utente = utenteService.findByUsername(authentication.getName());
        if (utente == null) {
            return "redirect:/";
        }
        model.addAttribute("utente", utente);
        model.addAttribute("errore", null);
        return "profiloForm";
    }

    @PostMapping("/profilo/modifica")
    public String salvaModifica(@ModelAttribute("username") String username,
                                 @ModelAttribute("password") String password,
                                 Authentication authentication,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 Model model) {

        Utente utente = utenteService.findByUsername(authentication.getName());
        if (utente == null) {
            return "redirect:/";
        }

        try {
            utenteService.aggiornaProfilo(utente.getId(), username, password);
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("utente", utente);
            model.addAttribute("errore", e.getMessage());
            return "profiloForm";
        }

        // Username o password sono cambiati: invalidiamo la sessione corrente,
        // l'utente dovra' accedere di nuovo con le nuove credenziali.
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/login?modificato=true";
    }

    @PostMapping("/profilo/elimina")
    public String elimina(Authentication authentication,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        Utente utente = utenteService.findByUsername(authentication.getName());
        if (utente != null) {
            utenteService.elimina(utente.getId());
        }

        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/?eliminato=true";
    }
}
