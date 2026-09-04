package it.uniroma3.siw.moviefestival.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.repository.FestivalRepository;

@Service
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public FestivalService(FestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    @Transactional(readOnly = true)
    public List<Festival> findAll() {
        return festivalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Festival findById(Long id) {
        return festivalRepository.findById(id).orElse(null);
    }

    @Transactional
    public Festival save(Festival festival) {
        return festivalRepository.save(festival);
    }

    @Transactional
    public void deleteById(Long id) {
        festivalRepository.deleteById(id);
    }

    // Metodi per l'analisi sperimentale delle strategie di fetch (sezione 8.2)

    @Transactional(readOnly = true)
    public List<Festival> findAllLazy() {
        return festivalRepository.findAllLazy();
    }

    @Transactional(readOnly = true)
    public List<Festival> findAllWithFilmJoinFetch() {
        return festivalRepository.findAllWithFilmJoinFetch();
    }

    @Transactional(readOnly = true)
    public List<Festival> findAllWithFilmEntityGraph() {
        return festivalRepository.findAllWithFilmEntityGraph();
    }
}