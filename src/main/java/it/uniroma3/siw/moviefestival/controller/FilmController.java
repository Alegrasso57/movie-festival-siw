package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.service.FilmService;
import it.uniroma3.siw.moviefestival.service.RecensioneService;

@Controller
public class FilmController {

    private final FilmService filmService;
    private final RecensioneService recensioneService;

    public FilmController(FilmService filmService, RecensioneService recensioneService) {
        this.filmService = filmService;
        this.recensioneService = recensioneService;
    }

    @GetMapping("/movies")
    public String elencoFilm(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "movies";
    }

    @GetMapping("/movie/{id}")
    public String dettaglioFilm(@PathVariable("id") Long id, Model model) {
        Film film = filmService.findById(id);
        if (film == null) {
            return "redirect:/movies";
        }
        model.addAttribute("film", film);
        model.addAttribute("recensioni", recensioneService.findByFilm(id));
        return "movieDetail";
    }
}