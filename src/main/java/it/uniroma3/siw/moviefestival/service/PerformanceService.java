package it.uniroma3.siw.moviefestival.service;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.dto.ConfrontoPerformanceDTO;
import it.uniroma3.siw.moviefestival.dto.RisultatoBenchmarkDTO;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.repository.FestivalRepository;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
public class PerformanceService {

    private final FilmRepository filmRepository;
    private final FestivalRepository festivalRepository;
    private final EntityManagerFactory entityManagerFactory;

    public PerformanceService(FilmRepository filmRepository, FestivalRepository festivalRepository,
                               EntityManagerFactory entityManagerFactory) {
        this.filmRepository = filmRepository;
        this.festivalRepository = festivalRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Transactional(readOnly = true)
    public ConfrontoPerformanceDTO confrontaStrategieFetch() {

        Statistics stats = entityManagerFactory.unwrap(org.hibernate.SessionFactory.class).getStatistics();
        boolean statisticheAttive = stats.isStatisticsEnabled();

        List<RisultatoBenchmarkDTO> risultatiFilmRegista = new ArrayList<>();
        risultatiFilmRegista.add(eseguiStrategiaFilm(stats, "LAZY", () -> filmRepository.findAllLazy()));
        risultatiFilmRegista.add(eseguiStrategiaFilm(stats, "JOIN FETCH", () -> filmRepository.findAllWithRegistaJoinFetch()));
        risultatiFilmRegista.add(eseguiStrategiaFilm(stats, "EntityGraph", () -> filmRepository.findAllWithRegistaEntityGraph()));

        // Secondo confronto, stavolta su una relazione @ManyToMany (una
        // collezione, non una singola entita' come Film-Regista): mostra
        // anche la duplicazione delle righe che un JOIN su una collezione
        // puo' introdurre, e perche' findAllWithFilmJoinFetch() usa DISTINCT
        // per riportarle a una riga per festival.
        List<RisultatoBenchmarkDTO> risultatiFestivalFilm = new ArrayList<>();
        risultatiFestivalFilm.add(eseguiStrategiaFestival(stats, "LAZY", () -> festivalRepository.findAllLazy()));
        risultatiFestivalFilm.add(eseguiStrategiaFestival(stats, "JOIN FETCH", () -> festivalRepository.findAllWithFilmJoinFetch()));
        risultatiFestivalFilm.add(eseguiStrategiaFestival(stats, "EntityGraph", () -> festivalRepository.findAllWithFilmEntityGraph()));

        return new ConfrontoPerformanceDTO(statisticheAttive, risultatiFilmRegista, risultatiFestivalFilm);
    }

    private RisultatoBenchmarkDTO eseguiStrategiaFilm(Statistics stats, String nomeStrategia, Supplier<List<Film>> operazione) {

        stats.clear();
        long inizio = System.currentTimeMillis();

        List<Film> film = operazione.get();
        for (Film f : film) {
            if (f.getRegista() != null) {
                f.getRegista().getNome();
            }
        }

        long tempoMs = System.currentTimeMillis() - inizio;
        long queryEseguite = stats.getPrepareStatementCount();

        return new RisultatoBenchmarkDTO(nomeStrategia, "Film caricati", film.size(), queryEseguite, tempoMs);
    }

    private RisultatoBenchmarkDTO eseguiStrategiaFestival(Statistics stats, String nomeStrategia, Supplier<List<Festival>> operazione) {

        stats.clear();
        long inizio = System.currentTimeMillis();

        List<Festival> festival = operazione.get();
        // Tocca la collezione "film" di ogni festival: se e' ancora LAZY,
        // e' qui che scatta la query aggiuntiva (una per festival).
        for (Festival f : festival) {
            f.getFilm().size();
        }

        long tempoMs = System.currentTimeMillis() - inizio;
        long queryEseguite = stats.getPrepareStatementCount();

        return new RisultatoBenchmarkDTO(nomeStrategia, "Festival caricati", festival.size(), queryEseguite, tempoMs);
    }
}
