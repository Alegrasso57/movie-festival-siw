package it.uniroma3.siw.moviefestival.service;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;
import java.util.List;
import java.util.function.Supplier;

@Service
public class PerformanceService {

    private final FilmRepository filmRepository;
    private final EntityManagerFactory entityManagerFactory;

    public PerformanceService(FilmRepository filmRepository, EntityManagerFactory entityManagerFactory) {
        this.filmRepository = filmRepository;
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
}