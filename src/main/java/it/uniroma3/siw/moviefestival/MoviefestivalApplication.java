package it.uniroma3.siw.moviefestival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviefestivalApplication {

	public static void main(String[] args) {
		// DIAGNOSTICA TEMPORANEA per capire se le variabili d'ambiente del
		// datasource arrivano davvero al container su Railway. Non stampa i
		// valori veri (niente password/URL in chiaro nei log), solo se sono
		// presenti e quanto sono lunghe. Da rimuovere una volta risolto.
		String[] chiaviDaControllare = {
				"SPRING_DATASOURCE_URL",
				"SPRING_DATASOURCE_USERNAME",
				"SPRING_DATASOURCE_PASSWORD"
		};
		for (String chiave : chiaviDaControllare) {
			String valore = System.getenv(chiave);
			System.out.println("[DIAGNOSTICA] " + chiave + " = "
					+ (valore == null ? "NON IMPOSTATA" : "presente (" + valore.length() + " caratteri)"));
		}

		SpringApplication.run(MoviefestivalApplication.class, args);
	}

}
