package it.uniroma3.siw.moviefestival.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.uniroma3.siw.moviefestival.dto.FestivalDTO;
import it.uniroma3.siw.moviefestival.dto.FilmDTO;
import it.uniroma3.siw.moviefestival.dto.ProiezioneDTO;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.service.FestivalService;

@RestController
@RequestMapping("/api")
public class FestivalRestController {

    private final FestivalService festivalService;

    public FestivalRestController(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @GetMapping("/festivals")
    public ResponseEntity<List<FestivalDTO>> getFestivals() {
        List<FestivalDTO> dto = festivalService.findAll()
                .stream()
                .map(FestivalDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/festivals/{id}")
    public ResponseEntity<FestivalDTO> getFestival(@PathVariable("id") Long id) {
        Festival festival = festivalService.findById(id);
        if (festival == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new FestivalDTO(festival));
    }

    @GetMapping("/festivals/{id}/movies")
    public ResponseEntity<List<FilmDTO>> getMoviesOfFestival(@PathVariable("id") Long id) {
        Festival festival = festivalService.findById(id);
        if (festival == null) {
            return ResponseEntity.notFound().build();
        }
        List<FilmDTO> dto = festival.getFilm()
                .stream()
                .map(FilmDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/festivals/{id}/screenings")
    public ResponseEntity<List<ProiezioneDTO>> getScreeningsOfFestival(@PathVariable("id") Long id) {
        Festival festival = festivalService.findById(id);
        if (festival == null) {
            return ResponseEntity.notFound().build();
        }
        List<ProiezioneDTO> dto = festival.getProiezioni()
                .stream()
                .map(ProiezioneDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}