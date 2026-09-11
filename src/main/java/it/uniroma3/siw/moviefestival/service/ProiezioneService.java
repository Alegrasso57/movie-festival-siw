package it.uniroma3.siw.moviefestival.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    // Formato italiano gg/mm/aaaa usato nei messaggi mostrati all'utente
    // (i LocalDate internamente restano in ISO, cambia solo la stampa).
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        return proiezioneRepository.findAllByOrderByDataAscOraAsc();
    }

    @Transactional(readOnly = true)
    public Proiezione findById(Long id) {
        return proiezioneRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public long count() {
        return proiezioneRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Proiezione> cerca(String titoloFilm) {
        return proiezioneRepository.findByFilm_TitoloContainingIgnoreCaseOrderByDataAscOraAsc(titoloFilm);
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

        validaFinestraTemporale(festival, data);

        boolean salaOccupata = proiezioneRepository.existsBySalaAndDataAndOra(sala, data, ora);
        if (salaOccupata) {
            throw new IllegalStateException(
                "La sala '" + sala.getNome() + "' è già occupata in data " + data.format(FORMATO_DATA) + " alle ore " + ora);
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

    /**
     * Modifica una proiezione esistente. Riusa le stesse verifiche di
     * consistenza della creazione (finestra temporale del festival, sala
     * libera), escludendo però la proiezione stessa dal controllo di
     * conflitto sulla sala — altrimenti risulterebbe sempre "occupata da
     * se stessa".
     */
    @Transactional
    public Proiezione modificaProiezione(Long id, Long festivalId, Long filmId, Long salaId,
                                          LocalDate data, LocalTime ora) {

        Proiezione proiezione = proiezioneRepository.findById(id).orElse(null);
        if (proiezione == null) {
            throw new IllegalArgumentException("Proiezione non trovata");
        }
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

        validaFinestraTemporale(festival, data);

        boolean salaOccupata = proiezioneRepository.existsBySalaAndDataAndOraAndIdNot(sala, data, ora, id);
        if (salaOccupata) {
            throw new IllegalStateException(
                "La sala '" + sala.getNome() + "' è già occupata in data " + data.format(FORMATO_DATA) + " alle ore " + ora);
        }

        proiezione.setFestival(festival);
        proiezione.setFilm(film);
        proiezione.setSala(sala);
        proiezione.setData(data);
        proiezione.setOra(ora);

        return proiezioneRepository.save(proiezione);
    }

    /**
     * Verifica di consistenza richiesta dal progetto: una proiezione non può
     * essere programmata al di fuori della finestra temporale (dataInizio -
     * dataFine) del festival a cui appartiene.
     */
    private void validaFinestraTemporale(Festival festival, LocalDate data) {
        if (data.isBefore(festival.getDataInizio()) || data.isAfter(festival.getDataFine())) {
            throw new IllegalStateException(
                "La data " + data.format(FORMATO_DATA) + " è fuori dalla finestra del festival '" + festival.getNome()
                    + "' (dal " + festival.getDataInizio().format(FORMATO_DATA) + " al " + festival.getDataFine().format(FORMATO_DATA) + ")");
        }
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
