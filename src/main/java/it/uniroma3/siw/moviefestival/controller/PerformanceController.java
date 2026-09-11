package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.siw.moviefestival.service.PerformanceService;

@Controller
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/admin/performance")
    public String testPerformance(Model model) {
        model.addAttribute("confronto", performanceService.confrontaStrategieFetch());
        return "admin/performance";
    }
}
