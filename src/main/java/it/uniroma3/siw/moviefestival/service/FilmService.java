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
       
        return filmRepository.findAllWithRegistaJoinFetch();
    }

    @Transactional(readOnly = true)
    public Film findById(Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public long count() {
        return filmRepository.count();
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

}
