package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.moviefestival.service.GenereService;

@Controller
public class GenereController {

    private final GenereService genereService;

    public GenereController(GenereService genereService) {
        this.genereService = genereService;
    }

    @GetMapping("/generi")
    public String elencoGeneri(@RequestParam(required = false) String nome, Model model) {
        if (nome != null && !nome.isBlank()) {
            model.addAttribute("generi", genereService.cerca(nome));
        } else {
            model.addAttribute("generi", genereService.findAll());
        }
        model.addAttribute("nome", nome);
        return "generi";
    }
}
