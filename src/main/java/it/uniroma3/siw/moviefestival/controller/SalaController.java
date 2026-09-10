package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.moviefestival.service.SalaService;

@Controller
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping("/sale")
    public String elencoSale(@RequestParam(required = false) String nome, Model model) {
        if (nome != null && !nome.isBlank()) {
            model.addAttribute("sale", salaService.cerca(nome));
        } else {
            model.addAttribute("sale", salaService.findAll());
        }
        model.addAttribute("nome", nome);
        return "sale";
    }
}
