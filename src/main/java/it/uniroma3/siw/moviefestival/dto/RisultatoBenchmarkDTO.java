package it.uniroma3.siw.moviefestival.dto;

/**
 * Un singolo risultato del confronto tra strategie di fetch (LAZY, JOIN
 * FETCH, EntityGraph): quante entita' sono state caricate, quante query SQL
 * Hibernate ha eseguito e quanto tempo e' servito. Usato solo per mostrare
 * i risultati nella pagina /admin/performance, non e' un'entita' JPA.
 */
public class RisultatoBenchmarkDTO {

    private final String nomeStrategia;
    private final String etichettaElementi;
    private final int elementiCaricati;
    private final long querySql;
    private final long tempoMs;

    public RisultatoBenchmarkDTO(String nomeStrategia, String etichettaElementi,
                                  int elementiCaricati, long querySql, long tempoMs) {
        this.nomeStrategia = nomeStrategia;
        this.etichettaElementi = etichettaElementi;
        this.elementiCaricati = elementiCaricati;
        this.querySql = querySql;
        this.tempoMs = tempoMs;
    }

    public String getNomeStrategia() {
        return nomeStrategia;
    }

    public String getEtichettaElementi() {
        return etichettaElementi;
    }

    public int getElementiCaricati() {
        return elementiCaricati;
    }

    public long getQuerySql() {
        return querySql;
    }

    public long getTempoMs() {
        return tempoMs;
    }
}
