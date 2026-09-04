package it.uniroma3.siw.moviefestival.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import it.uniroma3.siw.moviefestival.dto.RecensioneDTO;
import it.uniroma3.siw.moviefestival.model.Recensione;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.service.RecensioneService;
import it.uniroma3.siw.moviefestival.service.UtenteService;

@RestController
@RequestMapping("/api")
public class RecensioneRestController {

    private final RecensioneService recensioneService;
    private final UtenteService utenteService;

    public RecensioneRestController(RecensioneService recensioneService, UtenteService utenteService) {
        this.recensioneService = recensioneService;
        this.utenteService = utenteService;
    }

    @PostMapping("/movies/{id}/reviews")
    public ResponseEntity<?> creaRecensione(@PathVariable("id") Long filmId,
                                             @RequestBody Map<String, Object> body,
                                             Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("errore", "Non autenticato"));
        }

        Utente utente = utenteService.findByUsername(authentication.getName());
        if (utente == null) {
            return ResponseEntity.status(401).body(Map.of("errore", "Utente non trovato"));
        }

        String testo = (String) body.get("testo");
        Integer voto = (Integer) body.get("voto");

        try {
            Recensione recensione = recensioneService.creaRecensione(filmId, utente.getId(), testo, voto);
            return ResponseEntity.ok(new RecensioneDTO(recensione));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("errore", e.getMessage()));
        }
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<?> modificaRecensione(@PathVariable("id") Long id,
                                                 @RequestBody Map<String, Object> body,
                                                 Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("errore", "Non autenticato"));
        }

        Utente utente = utenteService.findByUsername(authentication.getName());
        String testo = (String) body.get("testo");
        Integer voto = (Integer) body.get("voto");

        try {
            boolean modificata = recensioneService.modificaRecensione(id, utente.getId(), testo, voto);
            if (!modificata) {
                return ResponseEntity.status(403).body(Map.of("errore", "Non autorizzato"));
            }
            return ResponseEntity.ok(new RecensioneDTO(recensioneService.findById(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> eliminaRecensione(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("errore", "Non autenticato"));
        }

        Utente utente = utenteService.findByUsername(authentication.getName());

        try {
            boolean eliminata = recensioneService.eliminaRecensione(id, utente.getId());
            if (!eliminata) {
                return ResponseEntity.status(403).body(Map.of("errore", "Non autorizzato"));
            }
            return ResponseEntity.ok(Map.of("messaggio", "Recensione eliminata"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}