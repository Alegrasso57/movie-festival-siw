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

        for (Long idFilm : idFilmDesiderati) {
            if (!idFilmAttuali.contains(idFilm)) {
                Film film = filmRepository.findById(idFilm).orElse(null);
                if (film != null) {
                    film.getFestival().add(festival);
                    filmRepository.save(film);
                }
            }
        }

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
