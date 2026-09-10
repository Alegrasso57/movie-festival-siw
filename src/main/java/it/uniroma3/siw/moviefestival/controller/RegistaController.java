package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.uniroma3.siw.moviefestival.service.RegistaService;

@Controller
public class RegistaController {

    private final RegistaService registaService;

    public RegistaController(RegistaService registaService) {
        this.registaService = registaService;
    }

    @GetMapping("/registi")
    public String elencoRegisti(@RequestParam(required = false) String cognome, Model model) {
        if (cognome != null && !cognome.isBlank()) {
            model.addAttribute("registi", registaService.cerca(cognome));
        } else {
            model.addAttribute("registi", registaService.findAll());
        }
        model.addAttribute("cognome", cognome);
        return "registi";
    }
}
