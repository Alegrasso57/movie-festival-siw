package it.uniroma3.siw.moviefestival.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.service.FestivalService;
import it.uniroma3.siw.moviefestival.service.FilmService;

@Controller
public class FestivalAdminController {

    private final FestivalService festivalService;
    private final FilmService filmService;

    public FestivalAdminController(FestivalService festivalService, FilmService filmService) {
        this.festivalService = festivalService;
        this.filmService = filmService;
    }

    @GetMapping("/admin/festival")
    public String elenco(Model model) {
        model.addAttribute("festivalList", festivalService.findAll());
        return "admin/festival";
    }

    @GetMapping("/admin/festival/nuovo")
    public String formNuovo(Model model) {
        model.addAttribute("festival", new Festival());
        model.addAttribute("filmDisponibili", filmService.findAll());
        model.addAttribute("erroreValidazione", false);
        return "admin/festivalForm";
    }

    @GetMapping("/admin/festival/{id}/modifica")
    public String formModifica(@PathVariable("id") Long id, Model model) {
        Festival festival = festivalService.findById(id);
        if (festival == null) {
            return "redirect:/admin/festival";
        }
        model.addAttribute("festival", festival);
        model.addAttribute("filmDisponibili", filmService.findAll());
        model.addAttribute("erroreValidazione", false);
        return "admin/festivalForm";
    }

    @PostMapping("/admin/festival")
    public String salva(@Valid @ModelAttribute("festival") Festival festivalForm,
                         BindingResult bindingResult,
                         @RequestParam(value = "filmIds", required = false) List<Long> filmIds,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("filmDisponibili", filmService.findAll());
            model.addAttribute("erroreValidazione", true);
            return "admin/festivalForm";
        }

        Festival festival;
        if (festivalForm.getId() != null) {
            festival = festivalService.findById(festivalForm.getId());
        } else {
            festival = new Festival();
        }

        festival.setNome(festivalForm.getNome());
        festival.setAnno(festivalForm.getAnno());
        festival.setCitta(festivalForm.getCitta());
        festival.setDataInizio(festivalForm.getDataInizio());
        festival.setDataFine(festivalForm.getDataFine());
        festival.setDescrizione(festivalForm.getDescrizione());

        List<Film> filmSelezionati = new ArrayList<>();
        if (filmIds != null) {
            for (Long filmId : filmIds) {
                Film film = filmService.findById(filmId);
                if (film != null) {
                    filmSelezionati.add(film);
                }
            }
        }
        festival.setFilm(filmSelezionati);

        festivalService.save(festival);
        return "redirect:/admin/festival";
    }

    @PostMapping("/admin/festival/{id}/elimina")
    public String elimina(@PathVariable("id") Long id) {
        festivalService.deleteById(id);
        return "redirect:/admin/festival";
    }
}