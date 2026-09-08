package it.uniroma3.siw.moviefestival.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.moviefestival.model.Proiezione;
import it.uniroma3.siw.moviefestival.model.Sala;

public interface ProiezioneRepository extends JpaRepository<Proiezione, Long> {

    boolean existsBySalaAndDataAndOra(Sala sala, LocalDate data, LocalTime ora);

    // Usato in fase di modifica: esclude la proiezione stessa dal controllo,
    // altrimenti risulterebbe sempre "in conflitto con se stessa".
    boolean existsBySalaAndDataAndOraAndIdNot(Sala sala, LocalDate data, LocalTime ora, Long id);

    List<Proiezione> findAllByOrderByDataAscOraAsc();
}
