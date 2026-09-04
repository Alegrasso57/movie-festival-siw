package it.uniroma3.siw.moviefestival.dto;

import it.uniroma3.siw.moviefestival.model.Festival;

public class FestivalDTO {

    private Long id;
    private String nome;
    private Integer anno;
    private String citta;
    private String dataInizio;
    private String dataFine;
    private String descrizione;

    public FestivalDTO(Festival festival) {
        this.id = festival.getId();
        this.nome = festival.getNome();
        this.anno = festival.getAnno();
        this.citta = festival.getCitta();
        this.dataInizio = festival.getDataInizio() != null ? festival.getDataInizio().toString() : null;
        this.dataFine = festival.getDataFine() != null ? festival.getDataFine().toString() : null;
        this.descrizione = festival.getDescrizione();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Integer getAnno() { return anno; }
    public String getCitta() { return citta; }
    public String getDataInizio() { return dataInizio; }
    public String getDataFine() { return dataFine; }
    public String getDescrizione() { return descrizione; }
}