package it.uniroma3.siw.moviefestival.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.moviefestival.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findByNomeContainingIgnoreCase(String nome);
}