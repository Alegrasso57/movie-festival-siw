package it.uniroma3.siw.moviefestival.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.moviefestival.model.Regista;

public interface RegistaRepository extends JpaRepository<Regista, Long> {

    List<Regista> findAllByOrderByCognomeAsc();

    List<Regista> findByCognomeContainingIgnoreCase(String cognome);
}