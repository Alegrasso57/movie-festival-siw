package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Il resource handler configurato in WebConfig serve correttamente i file
 * sotto /app/** (es. /app/assets/index-xyz.js) e le rotte client-side di
 * React Router che non esistono come file (es. /app/login, con fallback a
 * index.html). Ma la richiesta esatta "/app" (senza nulla dopo) viene
 * rifiutata da Spring prima ancora di interpellare quel resolver, perché il
 * percorso risultante all'interno del mapping è vuoto. Questo controller
 * intercetta solo questo caso e inoltra esplicitamente a /app/index.html.
 */
@Controller
public class ReactAppController {

    @GetMapping({"/app", "/app/"})
    public String paginaReact() {
        return "forward:/app/index.html";
    }
}
