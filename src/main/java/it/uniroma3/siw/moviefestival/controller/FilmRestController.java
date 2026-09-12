package it.uniroma3.siw.moviefestival.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.moviefestival.dto.FilmCreateDTO;
import it.uniroma3.siw.moviefestival.dto.FilmDTO;
import it.uniroma3.siw.moviefestival.dto.RecensioneDTO;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.model.Genere;
import it.uniroma3.siw.moviefestival.model.Regista;
import it.uniroma3.siw.moviefestival.service.FilmService;
import it.uniroma3.siw.moviefestival.service.GenereService;
import it.uniroma3.siw.moviefestival.service.RecensioneService;
import it.uniroma3.siw.moviefestival.service.RegistaService;

@RestController
@RequestMapping("/api")
public class FilmRestController {

    private final FilmService filmService;
    private final RecensioneService recensioneService;
    private final RegistaService registaService;
    private final GenereService genereService;

    public FilmRestController(FilmService filmService,
                               RecensioneService recensioneService,
                               RegistaService registaService,
                               GenereService genereService) {
        this.filmService = filmService;
        this.recensioneService = recensioneService;
        this.registaService = registaService;
        this.genereService = genereService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<FilmDTO>> getMovies() {
        List<FilmDTO> dto = filmService.findAll()
                .stream()
                .map(FilmDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<FilmDTO> getMovie(@PathVariable("id") Long id) {
        Film film = filmService.findById(id);
        if (film == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new FilmDTO(film));
    }

    @GetMapping("/movies/{id}/reviews")
    public ResponseEntity<List<RecensioneDTO>> getReviewsOfMovie(@PathVariable("id") Long id) {
        Film film = filmService.findById(id);
        if (film == null) {
            return ResponseEntity.notFound().build();
        }
        List<RecensioneDTO> dto = recensioneService.findByFilm(id)
                .stream()
                .map(RecensioneDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    
    @PostMapping("/movies")
    public ResponseEntity<?> creaFilm(@RequestBody FilmCreateDTO body) {

        if (body.getTitolo() == null || body.getTitolo().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("errore", "Il titolo è obbligatorio"));
        }
        if (body.getAnno() == null || body.getAnno() < 1888) {
            return ResponseEntity.badRequest().body(Map.of("errore", "Anno non valido"));
        }
        if (body.getDurata() == null || body.getDurata() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("errore", "Durata non valida"));
        }
        if (body.getGenere() == null || body.getGenere().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("errore", "Il genere è obbligatorio"));
        }
        if (body.getPaeseProduzione() == null || body.getPaeseProduzione().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("errore", "Il paese di produzione è obbligatorio"));
        }
        if (body.getRegistaId() == null) {
            return ResponseEntity.badRequest().body(Map.of("errore", "Il regista è obbligatorio"));
        }

        Regista regista = registaService.findById(body.getRegistaId());
        if (regista == null) {
            return ResponseEntity.badRequest().body(Map.of("errore", "Regista non trovato"));
        }

        
        Genere genere = genereService.trovaOCrea(body.getGenere());

        Film film = new Film();
        film.setTitolo(body.getTitolo());
        film.setAnno(body.getAnno());
        film.setDurata(body.getDurata());
        film.setGenere(genere);
        film.setPaeseProduzione(body.getPaeseProduzione());
        film.setRegista(regista);

        Film salvato = filmService.save(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FilmDTO(salvato));
    }
}
