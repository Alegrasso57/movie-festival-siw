package it.uniroma3.siw.moviefestival.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.model.Recensione;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;
import it.uniroma3.siw.moviefestival.repository.RecensioneRepository;
import it.uniroma3.siw.moviefestival.repository.UtenteRepository;

@Service
public class RecensioneService {

    private final RecensioneRepository recensioneRepository;
    private final FilmRepository filmRepository;
    private final UtenteRepository utenteRepository;

    public RecensioneService(RecensioneRepository recensioneRepository,
                              FilmRepository filmRepository,
                              UtenteRepository utenteRepository) {
        this.recensioneRepository = recensioneRepository;
        this.filmRepository = filmRepository;
        this.utenteRepository = utenteRepository;
    }

    @Transactional(readOnly = true)
    public List<Recensione> findByFilm(Long filmId) {
        Film film = filmRepository.findById(filmId).orElse(null);
        if (film == null) {
            throw new IllegalArgumentException("Film non trovato");
        }
        return recensioneRepository.findByFilm(film);
    }

    @Transactional(readOnly = true)
    public Recensione findById(Long id) {
        return recensioneRepository.findById(id).orElse(null);
    }

    private void validaTestoEVoto(String testo, Integer voto) {
        if (testo == null || testo.isBlank()) {
            throw new IllegalArgumentException("Il testo della recensione è obbligatorio");
        }
        if (voto == null || voto < 1 || voto > 10) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 10");
        }
    }

    @Transactional
    public Recensione creaRecensione(Long filmId, Long utenteId, String testo, Integer voto) {

        validaTestoEVoto(testo, voto);

        Film film = filmRepository.findById(filmId).orElse(null);
        if (film == null) {
            throw new IllegalArgumentException("Film non trovato");
        }

        Utente utente = utenteRepository.findById(utenteId).orElse(null);
        if (utente == null) {
            throw new IllegalArgumentException("Utente non trovato");
        }

        boolean giaRecensito = recensioneRepository.existsByFilmAndUtente(film, utente);
        if (giaRecensito) {
            throw new IllegalStateException("Hai già inserito una recensione per questo film");
        }

        Recensione recensione = new Recensione();
        recensione.setFilm(film);
        recensione.setUtente(utente);
        recensione.setTesto(testo);
        recensione.setVoto(voto);
        recensione.setData(LocalDate.now());

        return recensioneRepository.save(recensione);
    }

    @Transactional
    public boolean modificaRecensione(Long recensioneId, Long utenteId, String nuovoTesto, Integer nuovoVoto) {

        validaTestoEVoto(nuovoTesto, nuovoVoto);

        Recensione recensione = recensioneRepository.findById(recensioneId).orElse(null);
        if (recensione == null) {
            throw new IllegalArgumentException("Recensione non trovata");
        }

        if (!recensione.getUtente().getId().equals(utenteId)) {
            return false;
        }

        recensione.setTesto(nuovoTesto);
        recensione.setVoto(nuovoVoto);
        recensioneRepository.save(recensione);
        return true;
    }

    @Transactional
    public boolean eliminaRecensione(Long recensioneId, Long utenteId) {

        Recensione recensione = recensioneRepository.findById(recensioneId).orElse(null);
        if (recensione == null) {
            throw new IllegalArgumentException("Recensione non trovata");
        }

        if (!recensione.getUtente().getId().equals(utenteId)) {
            return false;
        }

        recensioneRepository.deleteById(recensioneId);
        return true;
    }
}