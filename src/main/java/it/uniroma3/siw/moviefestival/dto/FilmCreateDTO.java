package it.uniroma3.siw.moviefestival.dto;

public class FilmCreateDTO {

    private String titolo;
    private Integer anno;
    private Integer durata;
    private String genere;
    private String paeseProduzione;
    private Long registaId;

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public Integer getAnno() { return anno; }
    public void setAnno(Integer anno) { this.anno = anno; }

    public Integer getDurata() { return durata; }
    public void setDurata(Integer durata) { this.durata = durata; }

    public String getGenere() { return genere; }
    public void setGenere(String genere) { this.genere = genere; }

    public String getPaeseProduzione() { return paeseProduzione; }
    public void setPaeseProduzione(String paeseProduzione) { this.paeseProduzione = paeseProduzione; }

    public Long getRegistaId() { return registaId; }
    public void setRegistaId(Long registaId) { this.registaId = registaId; }
}
