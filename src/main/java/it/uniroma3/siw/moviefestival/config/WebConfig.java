package it.uniroma3.siw.moviefestival.config;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Serve la build statica del frontend React (cartella frontend/dist, generata
 * con "npm run build") sotto /app, così l'app React è raggiungibile dallo
 * stesso sito Spring Boot su :8080 invece che da un server separato.
 * Per lo sviluppo con hot-reload si continua a usare "npm run dev" su :5173
 * (che proxa le chiamate /api verso questo backend, vedi vite.config.ts).
 *
 * La cartella frontend/dist viene cercata a partire dalla working directory
 * del processo, risalendo fino a qualche livello superiore: IDE diversi
 * possono avviare l'applicazione con working directory diverse, quindi non
 * ci si affida a un singolo percorso relativo fisso. All'avvio viene
 * stampato un log che indica dove è stata trovata (o l'avviso se non lo è).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);
    private static final File FRONTEND_DIST_DIR = trovaCartellaDist();

    private static File trovaCartellaDist() {
        File base = new File(".").getAbsoluteFile();
        File candidata = null;
        for (int i = 0; i < 5 && base != null; i++) {
            candidata = new File(base, "frontend/dist");
            if (candidata.isDirectory() && new File(candidata, "index.html").isFile()) {
                log.info("WebConfig: cartella frontend/dist trovata in {}", candidata);
                return candidata;
            }
            base = base.getParentFile();
        }
        File fallback = new File("frontend/dist").getAbsoluteFile();
        log.warn("WebConfig: cartella frontend/dist NON trovata risalendo da {}. "
                + "La pagina React su /app non funzionerà finché non esegui 'npm run build' "
                + "dentro frontend/. Percorso che verrà comunque tentato: {}",
                new File(".").getAbsolutePath(), fallback);
        return fallback;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + FRONTEND_DIST_DIR.getAbsolutePath() + File.separator;
        Resource indexHtml = new FileSystemResource(new File(FRONTEND_DIST_DIR, "index.html"));

        registry.addResourceHandler("/app/**")
                .addResourceLocations(location)
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource loc) throws IOException {
                        // "/app" o "/app/" (nessun file richiesto): serve direttamente index.html.
                        if (resourcePath.isEmpty() || resourcePath.endsWith("/")) {
                            return indexHtml;
                        }

                        Resource richiesta = loc.createRelative(resourcePath);
                        if (richiesta.exists() && richiesta.isReadable()) {
                            return richiesta;
                        }

                        // Percorso lato client di React Router (es. /app/login): non
                        // esiste come file fisico, si restituisce index.html e sarà
                        // React Router a decidere quale pagina mostrare.
                        return indexHtml;
                    }
                });
    }
}
