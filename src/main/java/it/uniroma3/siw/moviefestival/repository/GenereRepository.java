package it.uniroma3.siw.moviefestival.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.moviefestival.model.Genere;

public interface GenereRepository extends JpaRepository<Genere, Long> {

    List<Genere> findAllByOrderByNomeAsc();

    List<Genere> findByNomeContainingIgnoreCase(String nome);

    Optional<Genere> findByNomeIgnoreCase(String nome);
}
