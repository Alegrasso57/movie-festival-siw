package it.uniroma3.siw.moviefestival.service;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.repository.FestivalRepository;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;
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
    public String confrontaStrategieFetch() {

        Statistics stats = entityManagerFactory.unwrap(org.hibernate.SessionFactory.class).getStatistics();

        StringBuilder risultato = new StringBuilder();
        risultato.append("=== Test accesso ai film del festival ===\n");
        risultato.append("(statistiche Hibernate attive: ").append(stats.isStatisticsEnabled()).append(")\n\n");

        risultato.append(eseguiStrategia(stats, "Strategia 1: LAZY", () -> filmRepository.findAllLazy()));
        risultato.append(eseguiStrategia(stats, "Strategia 2: JOIN FETCH", () -> filmRepository.findAllWithRegistaJoinFetch()));
        risultato.append(eseguiStrategia(stats, "Strategia 3: EntityGraph", () -> filmRepository.findAllWithRegistaEntityGraph()));

        // Secondo confronto, stavolta su una relazione @ManyToMany (una
        // collezione, non una singola entita come Film-Regista): mostra
        // anche la duplicazione delle righe che un JOIN su una collezione
        // puo introdurre, e perche findAllWithFilmJoinFetch() usa DISTINCT
        // per riportarle a una riga per festival.
        risultato.append("\n=== Test accesso ai film di un festival (collezione ManyToMany) ===\n\n");
        risultato.append(eseguiStrategiaFestival(stats, "Strategia 1: LAZY", () -> festivalRepository.findAllLazy()));
        risultato.append(eseguiStrategiaFestival(stats, "Strategia 2: JOIN FETCH", () -> festivalRepository.findAllWithFilmJoinFetch()));
        risultato.append(eseguiStrategiaFestival(stats, "Strategia 3: EntityGraph", () -> festivalRepository.findAllWithFilmEntityGraph()));

        return risultato.toString();
    }

    private String eseguiStrategia(Statistics stats, String nomeStrategia, Supplier<List<Film>> operazione) {

        stats.clear();

        long inizio = System.currentTimeMillis();

        List<Film> film = operazione.get();

        for (Film f : film) {
            if (f.getRegista() != null) {
                f.getRegista().getNome();
            }
        }

        long fine = System.currentTimeMillis();
        long tempoMs = fine - inizio;
        long queryEseguite = stats.getPrepareStatementCount();

        StringBuilder output = new StringBuilder();
        output.append(nomeStrategia).append("\n");
        output.append("Film caricati: ").append(film.size()).append("\n");
        output.append("Query SQL: ").append(queryEseguite).append("\n");
        output.append("Tempo: ").append(tempoMs).append(" ms\n\n");

        return output.toString();
    }

    private String eseguiStrategiaFestival(Statistics stats, String nomeStrategia, Supplier<List<Festival>> operazione) {

        stats.clear();

        long inizio = System.currentTimeMillis();

        List<Festival> festival = operazione.get();

        // Tocca la collezione "film" di ogni festival: se e ancora LAZY,
        // e qui che scatta la query aggiuntiva (una per festival).
        for (Festival f : festival) {
            f.getFilm().size();
        }

        long fine = System.currentTimeMillis();
        long tempoMs = fine - inizio;
        long queryEseguite = stats.getPrepareStatementCount();

        StringBuilder output = new StringBuilder();
        output.append(nomeStrategia).append("\n");
        output.append("Festival caricati: ").append(festival.size()).append("\n");
        output.append("Query SQL: ").append(queryEseguite).append("\n");
        output.append("Tempo: ").append(tempoMs).append(" ms\n\n");

        return output.toString();
    }
}