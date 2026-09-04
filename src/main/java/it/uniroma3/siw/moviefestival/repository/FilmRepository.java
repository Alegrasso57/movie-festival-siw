package it.uniroma3.siw.moviefestival.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import it.uniroma3.siw.moviefestival.model.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {

    // Strategia 1 — LAZY pura: nessun join, Hibernate userà il fetch di default
    @Query("SELECT f FROM Film f")
    List<Film> findAllLazy();

    // Strategia 2 — JOIN FETCH esplicito in JPQL: un'unica query con JOIN
    @Query("SELECT f FROM Film f LEFT JOIN FETCH f.regista")
    List<Film> findAllWithRegistaJoinFetch();

    // Strategia 3 — EntityGraph: approccio dichiarativo, stesso risultato della 2
    @EntityGraph(attributePaths = {"regista"})
    @Query("SELECT f FROM Film f")
    List<Film> findAllWithRegistaEntityGraph();
}