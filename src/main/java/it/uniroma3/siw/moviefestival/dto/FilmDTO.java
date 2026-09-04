package it.uniroma3.siw.moviefestival.dto;

import it.uniroma3.siw.moviefestival.model.Film;

public class FilmDTO {

    private Long id;
    private String titolo;
    private Integer anno;
    private Integer durata;
    private String genere;
    private String paeseProduzione;
    private String registaNome;

    public FilmDTO(Film film) {
        this.id = film.getId();
        this.titolo = film.getTitolo();
        this.anno = film.getAnno();
        this.durata = film.getDurata();
        this.genere = film.getGenere();
        this.paeseProduzione = film.getPaeseProduzione();
        if (film.getRegista() != null) {
            this.registaNome = film.getRegista().getNome() + " " + film.getRegista().getCognome();
        }
    }

    public Long getId() { return id; }
    public String getTitolo() { return titolo; }
    public Integer getAnno() { return anno; }
    public Integer getDurata() { return durata; }
    public String getGenere() { return genere; }
    public String getPaeseProduzione() { return paeseProduzione; }
    public String getRegistaNome() { return registaNome; }
}