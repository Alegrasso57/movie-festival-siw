package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.moviefestival.service.FestivalService;
import it.uniroma3.siw.moviefestival.service.FilmService;
import it.uniroma3.siw.moviefestival.service.GenereService;
import it.uniroma3.siw.moviefestival.service.ProiezioneService;
import it.uniroma3.siw.moviefestival.service.RegistaService;
import it.uniroma3.siw.moviefestival.service.SalaService;

@Controller
public class HomeController {

    private final FilmService filmService;
    private final FestivalService festivalService;
    private final ProiezioneService proiezioneService;
    private final SalaService salaService;
    private final RegistaService registaService;
    private final GenereService genereService;

    public HomeController(FilmService filmService,
                           FestivalService festivalService,
                           ProiezioneService proiezioneService,
                           SalaService salaService,
                           RegistaService registaService,
                           GenereService genereService) {
        this.filmService = filmService;
        this.festivalService = festivalService;
        this.proiezioneService = proiezioneService;
        this.salaService = salaService;
        this.registaService = registaService;
        this.genereService = genereService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("numeroFilm", filmService.count());
        model.addAttribute("numeroFestivals", festivalService.count());
        model.addAttribute("numeroRegisti", registaService.count());
        model.addAttribute("numeroProiezioni", proiezioneService.count());
        model.addAttribute("numeroSale", salaService.count());
        model.addAttribute("numeroGeneri", genereService.count());
        return "index";

    }
}
