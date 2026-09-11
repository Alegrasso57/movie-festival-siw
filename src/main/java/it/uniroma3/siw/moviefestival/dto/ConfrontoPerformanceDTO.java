package it.uniroma3.siw.moviefestival.dto;

import java.util.List;

/**
 * Raccoglie tutti i risultati del confronto tra strategie di fetch, cosi'
 * la pagina /admin/performance puo' mostrarli in modo leggibile invece di
 * un semplice blocco di testo.
 */
public class ConfrontoPerformanceDTO {

    private final boolean statisticheAttive;
    private final List<RisultatoBenchmarkDTO> risultatiFilmRegista;
    private final List<RisultatoBenchmarkDTO> risultatiFestivalFilm;

    public ConfrontoPerformanceDTO(boolean statisticheAttive,
                                    List<RisultatoBenchmarkDTO> risultatiFilmRegista,
                                    List<RisultatoBenchmarkDTO> risultatiFestivalFilm) {
        this.statisticheAttive = statisticheAttive;
        this.risultatiFilmRegista = risultatiFilmRegista;
        this.risultatiFestivalFilm = risultatiFestivalFilm;
    }

    public boolean isStatisticheAttive() {
        return statisticheAttive;
    }

    public List<RisultatoBenchmarkDTO> getRisultatiFilmRegista() {
        return risultatiFilmRegista;
    }

    public List<RisultatoBenchmarkDTO> getRisultatiFestivalFilm() {
        return risultatiFestivalFilm;
    }
}
