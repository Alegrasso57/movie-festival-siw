package it.uniroma3.siw.moviefestival.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        return "admin/festivalForm";
    }

    @PostMapping("/admin/festival")
    public String salva(@ModelAttribute("id") Long id,
                         @ModelAttribute("nome") String nome,
                         @ModelAttribute("anno") Integer anno,
                         @ModelAttribute("citta") String citta,
                         @ModelAttribute("dataInizio") String dataInizio,
                         @ModelAttribute("dataFine") String dataFine,
                         @ModelAttribute("descrizione") String descrizione,
                         @ModelAttribute("filmIds") List<Long> filmIds) {

        Festival festival;
        if (id != null) {
            festival = festivalService.findById(id);
        } else {
            festival = new Festival();
        }

        festival.setNome(nome);
        festival.setAnno(anno);
        festival.setCitta(citta);
        festival.setDataInizio(LocalDate.parse(dataInizio));
        festival.setDataFine(LocalDate.parse(dataFine));
        festival.setDescrizione(descrizione);

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