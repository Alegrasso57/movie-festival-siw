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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il titolo è obbligatorio")
    private String titolo;

    @NotNull(message = "L'anno è obbligatorio")
    @Min(value = 1888, message = "L'anno non può essere precedente al 1888 (nascita del cinema)")
    private Integer anno;

    @NotNull(message = "La durata è obbligatoria")
    @Positive(message = "La durata deve essere maggiore di zero")
    private Integer durata;

    @NotBlank(message = "Il genere è obbligatorio")
    private String genere;

    @NotBlank(message = "Il paese di produzione è obbligatorio")
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

    public Film() {
    }

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