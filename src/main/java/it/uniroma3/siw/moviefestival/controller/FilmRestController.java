package it.uniroma3.siw.moviefestival.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.uniroma3.siw.moviefestival.dto.FilmDTO;
import it.uniroma3.siw.moviefestival.dto.RecensioneDTO;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.service.FilmService;
import it.uniroma3.siw.moviefestival.service.RecensioneService;

@RestController
@RequestMapping("/api")
public class FilmRestController {

    private final FilmService filmService;
    private final RecensioneService recensioneService;

    public FilmRestController(FilmService filmService, RecensioneService recensioneService) {
        this.filmService = filmService;
        this.recensioneService = recensioneService;
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
}
