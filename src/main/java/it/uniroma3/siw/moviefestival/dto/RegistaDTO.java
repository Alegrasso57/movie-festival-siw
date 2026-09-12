package it.uniroma3.siw.moviefestival.dto;

import it.uniroma3.siw.moviefestival.model.Regista;

public class RegistaDTO {

    private final Long id;
    private final String nome;
    private final String cognome;

    public RegistaDTO(Regista regista) {
        this.id = regista.getId();
        this.nome = regista.getNome();
        this.cognome = regista.getCognome();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
}
