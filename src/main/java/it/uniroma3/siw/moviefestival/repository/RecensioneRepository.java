package it.uniroma3.siw.moviefestival.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.model.Recensione;
import it.uniroma3.siw.moviefestival.model.Utente;

public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    boolean existsByFilmAndUtente(Film film, Utente utente);

    List<Recensione> findByFilm(Film film);
}