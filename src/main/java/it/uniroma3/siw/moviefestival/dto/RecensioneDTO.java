package it.uniroma3.siw.moviefestival.dto;

import it.uniroma3.siw.moviefestival.model.Recensione;

public class RecensioneDTO {

    private Long id;
    private String testo;
    private Integer voto;
    private String data;
    private String autoreUsername;

    public RecensioneDTO(Recensione recensione) {
        this.id = recensione.getId();
        this.testo = recensione.getTesto();
        this.voto = recensione.getVoto();
        this.data = recensione.getData() != null ? recensione.getData().toString() : null;
        this.autoreUsername = recensione.getUtente() != null ? recensione.getUtente().getUsername() : null;
    }

    public Long getId() { return id; }
    public String getTesto() { return testo; }
    public Integer getVoto() { return voto; }
    public String getData() { return data; }
    public String getAutoreUsername() { return autoreUsername; }
}