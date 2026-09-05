package it.uniroma3.siw.moviefestival.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import it.uniroma3.siw.moviefestival.service.PerformanceService;

@Controller
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping(value = "/admin/performance", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String testPerformance() {
        return performanceService.confrontaStrategieFetch();
    }
}