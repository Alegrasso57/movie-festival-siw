package it.uniroma3.siw.moviefestival.dto;

import java.util.List;


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
