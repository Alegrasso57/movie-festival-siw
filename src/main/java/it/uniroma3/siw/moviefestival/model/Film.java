package it.uniroma3.siw.moviefestival.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titolo;
    private Integer anno;
    private Integer durata;
    private String genere;
    private String paeseProduzione;

    @ManyToOne
    private Regista regista;

    @ManyToMany
    @JoinTable(
        name = "film_festival",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "festival_id")
    )
    private List<Festival> festival = new ArrayList<>();

    @OneToMany(mappedBy = "film")
    private List<Proiezione> proiezioni = new ArrayList<>();

    @OneToMany(mappedBy = "film")
    private List<Recensione> recensioni = new ArrayList<>();

    // Costruttore vuoto, richiesto da JPA/Hibernate
    public Film() {
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getDurata() {
        return durata;
    }

    public void setDurata(Integer durata) {
        this.durata = durata;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getPaeseProduzione() {
        return paeseProduzione;
    }

    public void setPaeseProduzione(String paeseProduzione) {
        this.paeseProduzione = paeseProduzione;
    }

    public Regista getRegista() {
        return regista;
    }

    public void setRegista(Regista regista) {
        this.regista = regista;
    }

    public List<Festival> getFestival() {
        return festival;
    }

    public void setFestival(List<Festival> festival) {
        this.festival = festival;
    }

    public List<Proiezione> getProiezioni() {
        return proiezioni;
    }

    public void setProiezioni(List<Proiezione> proiezioni) {
        this.proiezioni = proiezioni;
    }

    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    public void setRecensioni(List<Recensione> recensioni) {
        this.recensioni = recensioni;
    }
}