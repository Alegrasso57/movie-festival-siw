package it.uniroma3.siw.moviefestival.dto;

import it.uniroma3.siw.moviefestival.model.Proiezione;

public class ProiezioneDTO {

    private Long id;
    private String data;
    private String ora;
    private String stato;
    private String filmTitolo;
    private String salaNome;

    public ProiezioneDTO(Proiezione proiezione) {
        this.id = proiezione.getId();
        this.data = proiezione.getData() != null ? proiezione.getData().toString() : null;
        this.ora = proiezione.getOra() != null ? proiezione.getOra().toString() : null;
        this.stato = proiezione.getStato() != null ? proiezione.getStato().name() : null;
        this.filmTitolo = proiezione.getFilm() != null ? proiezione.getFilm().getTitolo() : null;
        this.salaNome = proiezione.getSala() != null ? proiezione.getSala().getNome() : null;
    }

    public Long getId() { return id; }
    public String getData() { return data; }
    public String getOra() { return ora; }
    public String getStato() { return stato; }
    public String getFilmTitolo() { return filmTitolo; }
    public String getSalaNome() { return salaNome; }
}