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

    @Transactional
    public void deleteById(Long id) {
        registaRepository.deleteById(id);
    }
}