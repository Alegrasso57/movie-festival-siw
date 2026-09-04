package it.uniroma3.siw.moviefestival.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.moviefestival.model.Recensione;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.service.RecensioneService;
import it.uniroma3.siw.moviefestival.service.UtenteService;

@Controller
public class RecensioneController {

    private final RecensioneService recensioneService;
    private final UtenteService utenteService;

    public RecensioneController(RecensioneService recensioneService, UtenteService utenteService) {
        this.recensioneService = recensioneService;
        this.utenteService = utenteService;
    }

    @PostMapping("/movie/{id}/recensioni")
    public String creaRecensione(@PathVariable("id") Long filmId,
                                  @ModelAttribute("testo") String testo,
                                  @ModelAttribute("voto") Integer voto,
                                  Authentication authentication,
                                  Model model) {

        Utente utente = utenteService.findByUsername(authentication.getName());
        if (utente == null) {
            return "redirect:/login";
        }

        try {
            recensioneService.creaRecensione(filmId, utente.getId(), testo, voto);
        } catch (IllegalStateException e) {
            // L'utente ha già recensito questo film: ignoriamo silenziosamente per ora
        }

        return "redirect:/movie/" + filmId;
    }

    @GetMapping("/recensione/{id}/modifica")
    public String mostraFormModifica(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Recensione recensione = recensioneService.findById(id);
        if (recensione == null) {
            return "redirect:/movies";
        }

        if (!recensione.getUtente().getUsername().equals(authentication.getName())) {
            return "redirect:/movie/" + recensione.getFilm().getId();
        }

        model.addAttribute("recensione", recensione);
        return "recensioneEdit";
    }

    @PostMapping("/recensione/{id}/modifica")
    public String modificaRecensione(@PathVariable("id") Long id,
                                      @ModelAttribute("testo") String testo,
                                      @ModelAttribute("voto") Integer voto,
                                      Authentication authentication) {

        Utente utente = utenteService.findByUsername(authentication.getName());
        Recensione recensione = recensioneService.findById(id);
        if (recensione == null || utente == null) {
            return "redirect:/movies";
        }

        recensioneService.modificaRecensione(id, utente.getId(), testo, voto);
        return "redirect:/movie/" + recensione.getFilm().getId();
    }

    @PostMapping("/recensione/{id}/elimina")
    public String eliminaRecensione(@PathVariable("id") Long id, Authentication authentication) {

        Utente utente = utenteService.findByUsername(authentication.getName());
        Recensione recensione = recensioneService.findById(id);
        if (recensione == null || utente == null) {
            return "redirect:/movies";
        }

        Long filmId = recensione.getFilm().getId();
        recensioneService.eliminaRecensione(id, utente.getId());
        return "redirect:/movie/" + filmId;
    }
}