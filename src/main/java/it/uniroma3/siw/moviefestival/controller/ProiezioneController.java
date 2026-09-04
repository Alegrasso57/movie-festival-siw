package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.siw.moviefestival.service.ProiezioneService;

@Controller
public class ProiezioneController {

    private final ProiezioneService proiezioneService;

    public ProiezioneController(ProiezioneService proiezioneService) {
        this.proiezioneService = proiezioneService;
    }

    @GetMapping("/screenings")
    public String programmaProiezioni(Model model) {
        model.addAttribute("proiezioni", proiezioneService.findAll());
        return "screenings";
    }
}