package it.uniroma3.siw.moviefestival.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Transactional(readOnly = true)
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Film findById(Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    @Transactional
    public Film save(Film film) {
        return filmRepository.save(film);
    }

    @Transactional
    public void deleteById(Long id) {
        filmRepository.deleteById(id);
    }

    // Metodi per l'analisi sperimentale delle strategie di fetch (sezione 8.2)

    @Transactional(readOnly = true)
    public List<Film> findAllLazy() {
        return filmRepository.findAllLazy();
    }

    @Transactional(readOnly = true)
    public List<Film> findAllWithRegistaJoinFetch() {
        return filmRepository.findAllWithRegistaJoinFetch();
    }

    @Transactional(readOnly = true)
    public List<Film> findAllWithRegistaEntityGraph() {
        return filmRepository.findAllWithRegistaEntityGraph();
    }
}