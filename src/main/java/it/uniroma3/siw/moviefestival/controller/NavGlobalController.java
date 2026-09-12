package it.uniroma3.siw.moviefestival.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.moviefestival.service.FestivalService;
import it.uniroma3.siw.moviefestival.service.FilmService;
import it.uniroma3.siw.moviefestival.service.GenereService;
import it.uniroma3.siw.moviefestival.service.ProiezioneService;
import it.uniroma3.siw.moviefestival.service.RegistaService;
import it.uniroma3.siw.moviefestival.service.SalaService;

/**
 * Mette a disposizione di OGNI pagina (qualunque controller/qualunque
 * pagina restituita) il conteggio di ciascuna sezione del sito, cosi'
 * un domani basta aggiungere nella nav bar di un template una riga tipo
 *
 *     <a th:href="@{/movies}">Film (<span th:text="${numeroFilm}">0</span>)</a>
 *
 * senza dover toccare i singoli controller: Spring invoca questi metodi
 * @ModelAttribute prima di ogni richiesta e ne mette il risultato nel
 * Model automaticamente, qualunque sia il metodo/pagina invocato.
 *
 * Nota: essendo globale, questi metodi vengono eseguiti anche prima delle
 * chiamate ai @RestController (es. /api/films) anche se li' il Model non
 * viene mai letto/serializzato: significa qualche query COUNT(*) in piu'
 * non usata su quelle richieste. Per le dimensioni di questo progetto
 * (poche righe per tabella, uso locale) l'overhead e' trascurabile; se in
 * futuro servisse evitarlo, si puo' restringere l'advice con
 * @ControllerAdvice(assignableTypes = {...elenco dei soli controller "di pagina"...}).
 */
@ControllerAdvice
public class NavGlobalController {

    private final FilmService filmService;
    private final FestivalService festivalService;
    private final RegistaService registaService;
    private final ProiezioneService proiezioneService;
    private final SalaService salaService;
    private final GenereService genereService;

    public NavGlobalController(FilmService filmService,
                                FestivalService festivalService,
                                RegistaService registaService,
                                ProiezioneService proiezioneService,
                                SalaService salaService,
                                GenereService genereService) {
        this.filmService = filmService;
        this.festivalService = festivalService;
        this.registaService = registaService;
        this.proiezioneService = proiezioneService;
        this.salaService = salaService;
        this.genereService = genereService;
    }

    @ModelAttribute("numeroFilm")
    public long numeroFilm() {
        return filmService.count();
    }

    @ModelAttribute("numeroFestivals")
    public long numeroFestivals() {
        return festivalService.count();
    }

    @ModelAttribute("numeroRegisti")
    public long numeroRegisti() {
        return registaService.count();
    }

    @ModelAttribute("numeroProiezioni")
    public long numeroProiezioni() {
        return proiezioneService.count();
    }

    @ModelAttribute("numeroSale")
    public long numeroSale() {
        return salaService.count();
    }

    @ModelAttribute("numeroGeneri")
    public long numeroGeneri() {
        return genereService.count();
    }
}
