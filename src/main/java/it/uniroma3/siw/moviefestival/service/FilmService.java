package it.uniroma3.siw.moviefestival.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;
import it.uniroma3.siw.moviefestival.repository.ProiezioneRepository;
import it.uniroma3.siw.moviefestival.repository.RecensioneRepository;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final ProiezioneRepository proiezioneRepository;
    private final RecensioneRepository recensioneRepository;

    public FilmService(FilmRepository filmRepository,
                        ProiezioneRepository proiezioneRepository,
                        RecensioneRepository recensioneRepository) {
        this.filmRepository = filmRepository;
        this.proiezioneRepository = proiezioneRepository;
        this.recensioneRepository = recensioneRepository;
    }

    @Transactional(readOnly = true)
    public List<Film> findAll() {
        // Usa la query con JOIN FETCH invece del semplice findAll(): il
        // Regista viene caricato subito insieme al Film in un'unica query
        // SQL, evitando il problema N+1 dimostrato da PerformanceService
        // (vedi Strategia 1 vs Strategia 2 in /admin/performance).
        return filmRepository.findAllWithRegistaJoinFetch();
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
        Film film = filmRepository.findById(id).orElse(null);
        if (film == null) {
            return;
        }

        proiezioneRepository.deleteAll(film.getProiezioni());
        recensioneRepository.deleteAll(film.getRecensioni());

        for (Festival festival : new ArrayList<>(film.getFestival())) {
            festival.getFilm().remove(film);
        }

        filmRepository.delete(film);
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
