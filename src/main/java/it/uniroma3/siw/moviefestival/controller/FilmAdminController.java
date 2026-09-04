package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.model.Regista;
import it.uniroma3.siw.moviefestival.service.FilmService;
import it.uniroma3.siw.moviefestival.service.RegistaService;

@Controller
public class FilmAdminController {

    private final FilmService filmService;
    private final RegistaService registaService;

    public FilmAdminController(FilmService filmService, RegistaService registaService) {
        this.filmService = filmService;
        this.registaService = registaService;
    }

    @GetMapping("/admin/film")
    public String elenco(Model model) {
        model.addAttribute("filmList", filmService.findAll());
        return "admin/film";
    }

    @GetMapping("/admin/film/nuovo")
    public String formNuovo(Model model) {
        model.addAttribute("film", new Film());
        model.addAttribute("registi", registaService.findAll());
        return "admin/filmForm";
    }

    @GetMapping("/admin/film/{id}/modifica")
    public String formModifica(@PathVariable("id") Long id, Model model) {
        Film film = filmService.findById(id);
        if (film == null) {
            return "redirect:/admin/film";
        }
        model.addAttribute("film", film);
        model.addAttribute("registi", registaService.findAll());
        return "admin/filmForm";
    }

    @PostMapping("/admin/film")
    public String salva(@ModelAttribute("id") Long id,
                         @ModelAttribute("titolo") String titolo,
                         @ModelAttribute("anno") Integer anno,
                         @ModelAttribute("durata") Integer durata,
                         @ModelAttribute("genere") String genere,
                         @ModelAttribute("paeseProduzione") String paeseProduzione,
                         @ModelAttribute("registaId") Long registaId) {

        Film film;
        if (id != null) {
            film = filmService.findById(id);
        } else {
            film = new Film();
        }

        film.setTitolo(titolo);
        film.setAnno(anno);
        film.setDurata(durata);
        film.setGenere(genere);
        film.setPaeseProduzione(paeseProduzione);

        Regista regista = registaService.findById(registaId);
        film.setRegista(regista);

        filmService.save(film);
        return "redirect:/admin/film";
    }

    @PostMapping("/admin/film/{id}/elimina")
    public String elimina(@PathVariable("id") Long id) {
        filmService.deleteById(id);
        return "redirect:/admin/film";
    }
}