package it.uniroma3.siw.moviefestival.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.model.Proiezione;
import it.uniroma3.siw.moviefestival.model.Sala;
import it.uniroma3.siw.moviefestival.model.StatoProiezione;
import it.uniroma3.siw.moviefestival.repository.FestivalRepository;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;
import it.uniroma3.siw.moviefestival.repository.ProiezioneRepository;
import it.uniroma3.siw.moviefestival.repository.SalaRepository;

@Service
public class ProiezioneService {

    private final ProiezioneRepository proiezioneRepository;
    private final FestivalRepository festivalRepository;
    private final FilmRepository filmRepository;
    private final SalaRepository salaRepository;

    public ProiezioneService(ProiezioneRepository proiezioneRepository,
                              FestivalRepository festivalRepository,
                              FilmRepository filmRepository,
                              SalaRepository salaRepository) {
        this.proiezioneRepository = proiezioneRepository;
        this.festivalRepository = festivalRepository;
        this.filmRepository = filmRepository;
        this.salaRepository = salaRepository;
    }

    @Transactional(readOnly = true)
    public List<Proiezione> findAll() {
        return proiezioneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Proiezione findById(Long id) {
        return proiezioneRepository.findById(id).orElse(null);
    }

    @Transactional
    public Proiezione creaProiezione(Long festivalId, Long filmId, Long salaId,
                                      LocalDate data, LocalTime ora) {

        if (data == null) {
            throw new IllegalArgumentException("La data è obbligatoria");
        }
        if (ora == null) {
            throw new IllegalArgumentException("L'ora è obbligatoria");
        }

        Festival festival = festivalRepository.findById(festivalId).orElse(null);
        if (festival == null) {
            throw new IllegalArgumentException("Festival non trovato");
        }

        Film film = filmRepository.findById(filmId).orElse(null);
        if (film == null) {
            throw new IllegalArgumentException("Film non trovato");
        }

        Sala sala = salaRepository.findById(salaId).orElse(null);
        if (sala == null) {
            throw new IllegalArgumentException("Sala non trovata");
        }

        boolean salaOccupata = proiezioneRepository.existsBySalaAndDataAndOra(sala, data, ora);
        if (salaOccupata) {
            throw new IllegalStateException(
                "La sala '" + sala.getNome() + "' è già occupata in data " + data + " alle ore " + ora);
        }

        Proiezione proiezione = new Proiezione();
        proiezione.setFestival(festival);
        proiezione.setFilm(film);
        proiezione.setSala(sala);
        proiezione.setData(data);
        proiezione.setOra(ora);
        proiezione.setStato(StatoProiezione.SCHEDULED);

        return proiezioneRepository.save(proiezione);
    }

    @Transactional
    public Proiezione save(Proiezione proiezione) {
        return proiezioneRepository.save(proiezione);
    }

    @Transactional
    public void deleteById(Long id) {
        proiezioneRepository.deleteById(id);
    }
}