package it.uniroma3.siw.moviefestival.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Genere;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;
import it.uniroma3.siw.moviefestival.repository.GenereRepository;

@Service
public class GenereService {

    private final GenereRepository genereRepository;
    private final FilmRepository filmRepository;

    public GenereService(GenereRepository genereRepository, FilmRepository filmRepository) {
        this.genereRepository = genereRepository;
        this.filmRepository = filmRepository;
    }

    @Transactional(readOnly = true)
    public List<Genere> findAll() {
        return genereRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public Genere findById(Long id) {
        return genereRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Genere> cerca(String nome) {
        return genereRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public long count() {
        return genereRepository.count();
    }

    @Transactional(readOnly = true)
    public long contaFilm(Genere genere) {
        return filmRepository.countByGenere(genere);
    }

    @Transactional
    public Genere save(Genere genere) {
        return genereRepository.save(genere);
    }

    /**
     * Trova un genere esistente per nome (case-insensitive) o lo crea se non
     * esiste ancora. Usato dalla creazione rapida di un film via REST (form
     * React "Nuovo film"), che continua ad accettare il genere come testo
     * libero invece di un menu a tendina.
     */
    @Transactional
    public Genere trovaOCrea(String nome) {
        if (nome == null || nome.isBlank()) {
            return null;
        }
        return genereRepository.findByNomeIgnoreCase(nome.trim())
                .orElseGet(() -> genereRepository.save(new Genere(nome.trim())));
    }

    /**
     * Un film deve sempre avere un genere (vedi Film.genere): non possiamo
     * quindi eliminare un genere ancora associato a uno o più film, altrimenti
     * quei film resterebbero senza genere o l'eliminazione fallirebbe a
     * livello di database per un vincolo di integrità referenziale.
     */
    @Transactional
    public void deleteById(Long id) {
        Genere genere = genereRepository.findById(id).orElse(null);
        if (genere == null) {
            return;
        }

        long filmAssociati = filmRepository.countByGenere(genere);
        if (filmAssociati > 0) {
            throw new IllegalStateException(
                    "Impossibile eliminare '" + genere.getNome() + "': è ancora usato da "
                            + filmAssociati + " film. Riassegna o elimina prima quei film.");
        }

        genereRepository.delete(genere);
    }
}
