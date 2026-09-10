package it.uniroma3.siw.moviefestival.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Sala;
import it.uniroma3.siw.moviefestival.repository.SalaRepository;

@Service
public class SalaService {

    private final SalaRepository salaRepository;

    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @Transactional(readOnly = true)
    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Sala findById(Long id) {
        return salaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public long count() {
        return salaRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Sala> cerca(String nome) {
        return salaRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public Sala save(Sala sala) {
        return salaRepository.save(sala);
    }

    /**
     * Una proiezione deve sempre avere una sala (relazione obbligatoria,
     * vedi Proiezione.sala): non possiamo eliminare una sala che ha ancora
     * proiezioni programmate, altrimenti l'eliminazione fallirebbe a
     * livello di database con un errore di integrità referenziale
     * (Whitelabel Error Page). Blocchiamo l'operazione con un messaggio
     * chiaro per l'admin: vanno eliminate prima quelle proiezioni.
     */
    @Transactional
    public void deleteById(Long id) {
        Sala sala = salaRepository.findById(id).orElse(null);
        if (sala == null) {
            return;
        }

        if (!sala.getProiezioni().isEmpty()) {
            throw new IllegalStateException(
                    "Impossibile eliminare la sala '" + sala.getNome() + "': ha ancora "
                            + sala.getProiezioni().size()
                            + " proiezioni programmate. Elimina prima quelle proiezioni.");
        }

        salaRepository.delete(sala);
    }
}
