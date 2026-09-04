package it.uniroma3.siw.moviefestival;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import it.uniroma3.siw.moviefestival.model.Festival;
import it.uniroma3.siw.moviefestival.model.Film;
import it.uniroma3.siw.moviefestival.model.Regista;
import it.uniroma3.siw.moviefestival.model.Ruolo;
import it.uniroma3.siw.moviefestival.model.Sala;
import it.uniroma3.siw.moviefestival.model.Utente;
import it.uniroma3.siw.moviefestival.repository.FestivalRepository;
import it.uniroma3.siw.moviefestival.repository.FilmRepository;
import it.uniroma3.siw.moviefestival.repository.RegistaRepository;
import it.uniroma3.siw.moviefestival.repository.SalaRepository;
import it.uniroma3.siw.moviefestival.repository.UtenteRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RegistaRepository registaRepository;
    private final FilmRepository filmRepository;
    private final FestivalRepository festivalRepository;
    private final SalaRepository salaRepository;
    private final UtenteRepository utenteRepository;

    public DataInitializer(RegistaRepository registaRepository,
                            FilmRepository filmRepository,
                            FestivalRepository festivalRepository,
                            SalaRepository salaRepository,
                            UtenteRepository utenteRepository) {
        this.registaRepository = registaRepository;
        this.filmRepository = filmRepository;
        this.festivalRepository = festivalRepository;
        this.salaRepository = salaRepository;
        this.utenteRepository = utenteRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Evita di duplicare i dati ad ogni riavvio dell'applicazione
        if (registaRepository.count() > 0) {
            return;
        }

        // Registi
        Regista nolan = new Regista();
        nolan.setNome("Christopher");
        nolan.setCognome("Nolan");
        nolan.setDataNascita(LocalDate.of(1970, 7, 30));
        nolan.setNazionalita("Britannica");
        registaRepository.save(nolan);

        Regista bong = new Regista();
        bong.setNome("Bong");
        bong.setCognome("Joon-ho");
        bong.setDataNascita(LocalDate.of(1969, 9, 14));
        bong.setNazionalita("Sudcoreana");
        registaRepository.save(bong);

        // Film
        Film inception = new Film();
        inception.setTitolo("Inception");
        inception.setAnno(2010);
        inception.setDurata(148);
        inception.setGenere("Fantascienza");
        inception.setPaeseProduzione("USA");
        inception.setRegista(nolan);
        filmRepository.save(inception);

        Film parasite = new Film();
        parasite.setTitolo("Parasite");
        parasite.setAnno(2019);
        parasite.setDurata(132);
        parasite.setGenere("Thriller");
        parasite.setPaeseProduzione("Corea del Sud");
        parasite.setRegista(bong);
        filmRepository.save(parasite);

        // Festival
        Festival venezia = new Festival();
        venezia.setNome("Mostra del Cinema di Venezia");
        venezia.setAnno(2026);
        venezia.setCitta("Venezia");
        venezia.setDataInizio(LocalDate.of(2026, 9, 1));
        venezia.setDataFine(LocalDate.of(2026, 9, 11));
        venezia.setDescrizione("Uno dei festival cinematografici più antichi al mondo.");

        venezia.getFilm().add(inception);
        venezia.getFilm().add(parasite);
        festivalRepository.save(venezia);

        inception.getFestival().add(venezia);
        parasite.getFestival().add(venezia);
        filmRepository.save(inception);
        filmRepository.save(parasite);

        // Sala
        Sala sala1 = new Sala();
        sala1.setNome("Sala Grande");
        sala1.setIndirizzo("Lungomare Marconi, Venezia");
        sala1.setCapienza(1000);
        salaRepository.save(sala1);

        // Utente amministratore di test
        Utente admin = new Utente();
        admin.setUsername("admin");
        admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
        admin.setRuolo(Ruolo.ADMIN);
        utenteRepository.save(admin);

        System.out.println(">>> Dati di prova inseriti correttamente.");
    }
}