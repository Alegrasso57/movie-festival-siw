package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ReactAppController {

    @GetMapping({"/app", "/app/"})
    public String paginaReact() {
        return "forward:/app/index.html";
    }
}
