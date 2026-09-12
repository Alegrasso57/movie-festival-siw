package it.uniroma3.siw.moviefestival.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.model.Genere;

public interface FilmRepository extends JpaRepository<Film, Long> {

    long countByGenere(Genere genere);

    @Query("SELECT f FROM Film f")
    List<Film> findAllLazy();


    @Query("SELECT f FROM Film f LEFT JOIN FETCH f.regista")
    List<Film> findAllWithRegistaJoinFetch();


    @EntityGraph(attributePaths = {"regista"})
    @Query("SELECT f FROM Film f")
    List<Film> findAllWithRegistaEntityGraph();

}