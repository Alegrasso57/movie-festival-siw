package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.service.FestivalService;

@Controller
public class FestivalController {

    private final FestivalService festivalService;

    public FestivalController(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @GetMapping("/festivals")
    public String elencoFestival(Model model) {
        model.addAttribute("festivals", festivalService.findAll());
        return "festivals";
    }

    @GetMapping("/festival/{id}")
    public String dettaglioFestival(@PathVariable("id") Long id, Model model) {
        Festival festival = festivalService.findById(id);
        if (festival == null) {
            return "redirect:/festivals";
        }
        model.addAttribute("festival", festival);
        return "festivalDetail";
    }
}