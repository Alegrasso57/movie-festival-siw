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

    @Transactional
    public Sala save(Sala sala) {
        return salaRepository.save(sala);
    }

    @Transactional
    public void deleteById(Long id) {
        salaRepository.deleteById(id);
    }
}