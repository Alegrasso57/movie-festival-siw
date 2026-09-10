package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.moviefestival.service.ProiezioneService;

@Controller
public class ProiezioneController {

    private final ProiezioneService proiezioneService;

    public ProiezioneController(ProiezioneService proiezioneService) {
        this.proiezioneService = proiezioneService;
    }

    @GetMapping("/screenings")
    public String programmaProiezioni(@RequestParam(required = false) String film, Model model) {
        if (film != null && !film.isBlank()) {
            model.addAttribute("proiezioni", proiezioneService.cerca(film));
        } else {
            model.addAttribute("proiezioni", proiezioneService.findAll());
        }
        model.addAttribute("film", film);
        return "screenings";
    }
}