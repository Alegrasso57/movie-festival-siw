package it.uniroma3.siw.moviefestival.dto;

import it.uniroma3.siw.moviefestival.model.Film;

public class FilmDTO {

    private final Long id;
    private final String titolo;
    private final Integer anno;
    private final Integer durata;
    private final String genere;
    private final String paeseProduzione;
    private final String registaNome;

    public FilmDTO(Film film) {
        this.id = film.getId();
        this.titolo = film.getTitolo();
        this.anno = film.getAnno();
        this.durata = film.getDurata();
        this.genere = film.getGenere() != null ? film.getGenere().getNome() : null;
        this.paeseProduzione = film.getPaeseProduzione();
        this.registaNome = film.getRegista() != null
                ? film.getRegista().getNome() + " " + film.getRegista().getCognome()
                : null;
    }

    public Long getId() { return id; }
    public String getTitolo() { return titolo; }
    public Integer getAnno() { return anno; }
    public Integer getDurata() { return durata; }
    public String getGenere() { return genere; }
    public String getPaeseProduzione() { return paeseProduzione; }
    public String getRegistaNome() { return registaNome; }
}
