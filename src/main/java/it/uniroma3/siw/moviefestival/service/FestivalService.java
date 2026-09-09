package it.uniroma3.siw.moviefestival.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.repository.FestivalRepository;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;

@Service
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final FilmRepository filmRepository;

    public FestivalService(FestivalRepository festivalRepository, FilmRepository filmRepository) {
        this.festivalRepository = festivalRepository;
        this.filmRepository = filmRepository;
    }

    @Transactional(readOnly = true)
    public List<Festival> findAll() {
        // Come per FilmService.findAll(): usa la query con JOIN FETCH
        // invece del semplice findAll(), cosi la collezione "film" (una
        // @ManyToMany, LAZY di default) viene caricata subito insieme ai
        // festival in un'unica query, evitando l'N+1 se in futuro si
        // accede a festival.getFilm() per ognuno (es. per contarli in una
        // pagina di elenco).
        return festivalRepository.findAllWithFilmJoinFetch();
    }

    @Transactional(readOnly = true)
    public Festival findById(Long id) {
        return festivalRepository.findById(id).orElse(null);
    }

    @Transactional
    public Festival save(Festival festival) {
        return festivalRepository.save(festival);
    }

    /**
     * Salva i dati del festival e allinea i film partecipanti alla lista
     * scelta nel form (filmIds).
     *
     * Nota importante sul mapping JPA: la relazione ManyToMany tra Film e
     * Festival è mappata con la tabella di join sul lato Film
     * (@JoinTable in Film.festival). Questo rende Film il lato
     * "proprietario" della relazione: Hibernate scrive la tabella di join
     * SOLO quando si modifica quella collezione. Festival.film è invece
     * dichiarata con mappedBy — è il lato "inverso", di sola lettura ai
     * fini della persistenza: assegnare una lista a festival.setFilm(...) e
     * salvare il festival non scrive nulla nella tabella di join (bug che
     * causava "Film partecipanti" sempre vuoto). Per aggiornare
     * l'associazione bisogna quindi intervenire sulla collezione
     * Film.festival di ciascun film coinvolto.
     */
    @Transactional
    public Festival save(Festival datiFestival, List<Long> filmIds) {

        Festival festival;
        if (datiFestival.getId() != null) {
            festival = festivalRepository.findById(datiFestival.getId()).orElse(new Festival());
        } else {
            festival = new Festival();
        }

        festival.setNome(datiFestival.getNome());
        festival.setAnno(datiFestival.getAnno());
        festival.setCitta(datiFestival.getCitta());
        festival.setDataInizio(datiFestival.getDataInizio());
        festival.setDataFine(datiFestival.getDataFine());
        festival.setDescrizione(datiFestival.getDescrizione());

        festival = festivalRepository.save(festival);

        Set<Long> idFilmDesiderati = filmIds == null ? new HashSet<>() : new HashSet<>(filmIds);
        Set<Long> idFilmAttuali = festival.getFilm().stream()
                .map(Film::getId)
                .collect(Collectors.toSet());

        // Aggiunge il festival ai film appena selezionati che non lo avevano ancora
        for (Long idFilm : idFilmDesiderati) {
            if (!idFilmAttuali.contains(idFilm)) {
                Film film = filmRepository.findById(idFilm).orElse(null);
                if (film != null) {
                    film.getFestival().add(festival);
                    filmRepository.save(film);
                }
            }
        }

        // Toglie il festival dai film che erano selezionati e ora non lo sono più
        for (Film film : new ArrayList<>(festival.getFilm())) {
            if (!idFilmDesiderati.contains(film.getId())) {
                film.getFestival().remove(festival);
                filmRepository.save(film);
            }
        }

        return festival;
    }

    @Transactional
    public void deleteById(Long id) {
        festivalRepository.deleteById(id);
    }

}
