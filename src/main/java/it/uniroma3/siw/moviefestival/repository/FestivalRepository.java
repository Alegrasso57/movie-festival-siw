package it.uniroma3.siw.moviefestival.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import it.uniroma3.siw.moviefestival.model.Festival;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

    // Strategia 1 — LAZY pura
    @Query("SELECT f FROM Festival f")
    List<Festival> findAllLazy();

    // Strategia 2 — JOIN FETCH esplicito. DISTINCT necessario: il JOIN sulla
    // relazione molti-a-molti moltiplicherebbe le righe di Festival
    // (una per ogni film associato), DISTINCT le riporta a una per festival.
    @Query("SELECT DISTINCT f FROM Festival f LEFT JOIN FETCH f.film")
    List<Festival> findAllWithFilmJoinFetch();

    // Strategia 3 — EntityGraph
    @EntityGraph(attributePaths = {"film"})
    @Query("SELECT f FROM Festival f")
    List<Festival> findAllWithFilmEntityGraph();
}