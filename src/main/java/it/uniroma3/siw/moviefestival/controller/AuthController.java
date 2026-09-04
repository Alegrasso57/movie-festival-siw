package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.moviefestival.service.UtenteService;

@Controller
public class AuthController {

    private final UtenteService utenteService;

    public AuthController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/login")
    public String mostraLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String mostraRegistrazione(Model model) {
        model.addAttribute("errore", null);
        return "register";
    }

    @PostMapping("/register")
    public String registraUtente(@ModelAttribute("username") String username,
                                  @ModelAttribute("password") String password,
                                  Model model) {
        try {
            utenteService.registra(username, password);
            return "redirect:/login";
        } catch (IllegalStateException e) {
            model.addAttribute("errore", e.getMessage());
            return "register";
        }
    }
}