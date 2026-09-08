package it.uniroma3.siw.moviefestival.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.uniroma3.siw.moviefestival.dto.RegistaDTO;
import it.uniroma3.siw.moviefestival.service.RegistaService;

@RestController
@RequestMapping("/api")
public class RegistaRestController {

    private final RegistaService registaService;

    public RegistaRestController(RegistaService registaService) {
        this.registaService = registaService;
    }

    @GetMapping("/registi")
    public ResponseEntity<List<RegistaDTO>> getRegisti() {
        List<RegistaDTO> dto = registaService.findAll()
                .stream()
                .map(RegistaDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}
