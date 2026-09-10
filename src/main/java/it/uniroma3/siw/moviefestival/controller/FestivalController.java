package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.service.FestivalService;

@Controller
public class FestivalController {

    private final FestivalService festivalService;

    public FestivalController(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @GetMapping("/festivals")
    public String elencoFestival(@RequestParam(required = false) String nome, Model model) {
        if (nome != null && !nome.isBlank()) {
            model.addAttribute("festivals", festivalService.cerca(nome));
        } else {
            model.addAttribute("festivals", festivalService.findAll());
        }
        model.addAttribute("nome", nome);
        return "festivals/list";
    }

    @GetMapping("/festival/{id}")
    public String dettaglioFestival(@PathVariable("id") Long id, Model model) {
        Festival festival = festivalService.findById(id);
        if (festival == null) {
            return "redirect:/festivals";
        }
        model.addAttribute("festival", festival);
        return "festivals/show";
    }

}