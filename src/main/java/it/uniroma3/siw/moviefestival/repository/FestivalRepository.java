package it.uniroma3.siw.moviefestival.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.uniroma3.siw.moviefestival.model.Festival;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

    @Query("SELECT f FROM Festival f")
    List<Festival> findAllLazy();

    @Query("SELECT DISTINCT f FROM Festival f LEFT JOIN FETCH f.film")
    List<Festival> findAllWithFilmJoinFetch();

    @EntityGraph(attributePaths = {"film"})
    @Query("SELECT f FROM Festival f")
    List<Festival> findAllWithFilmEntityGraph();

    List<Festival> findByNomeContainingIgnoreCase(String nome);

}