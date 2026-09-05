package it.uniroma3.siw.moviefestival.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("erroreValidazione", false);
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
        model.addAttribute("erroreValidazione", false);
        return "admin/filmForm";
    }

    @PostMapping("/admin/film")
    public String salva(@Valid @ModelAttribute Film filmForm,
                         BindingResult bindingResult,
                         @ModelAttribute("registaId") Long registaId,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("registi", registaService.findAll());
            model.addAttribute("film", filmForm);
            model.addAttribute("erroreValidazione", true);
            return "admin/filmForm";
        }

        Film film;
        if (filmForm.getId() != null) {
            film = filmService.findById(filmForm.getId());
        } else {
            film = new Film();
        }

        film.setTitolo(filmForm.getTitolo());
        film.setAnno(filmForm.getAnno());
        film.setDurata(filmForm.getDurata());
        film.setGenere(filmForm.getGenere());
        film.setPaeseProduzione(filmForm.getPaeseProduzione());

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