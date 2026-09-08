package it.uniroma3.siw.moviefestival.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Regista;
import it.uniroma3.siw.moviefestival.repository.RegistaRepository;

@Service
public class RegistaService {

    private final RegistaRepository registaRepository;

    public RegistaService(RegistaRepository registaRepository) {
        this.registaRepository = registaRepository;
    }

    @Transactional(readOnly = true)
    public List<Regista> findAll() {
        return registaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Regista findById(Long id) {
        return registaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Regista save(Regista regista) {
        return registaRepository.save(regista);
    }

    /**
     * Un film deve sempre avere un regista (relazione obbligatoria, vedi
     * Film.regista): non possiamo quindi eliminare un regista che è ancora
     * associato a uno o più film, altrimenti quei film resterebbero senza
     * regista (violando il vincolo) o l'eliminazione fallirebbe a livello di
     * database con un errore di integrità referenziale (Whitelabel Error
     * Page). Blocchiamo l'operazione con un messaggio chiaro per l'admin:
     * prima va riassegnato o eliminato ogni film di questo regista.
     */
    @Transactional
    public void deleteById(Long id) {
        Regista regista = registaRepository.findById(id).orElse(null);
        if (regista == null) {
            return;
        }

        if (!regista.getFilm().isEmpty()) {
            throw new IllegalStateException(
                    "Impossibile eliminare " + regista.getNome() + " " + regista.getCognome()
                            + ": è ancora regista di " + regista.getFilm().size()
                            + " film. Riassegna o elimina prima quei film.");
        }

        registaRepository.delete(regista);
    }
}
