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

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);
    private static final File FRONTEND_DIST_DIR = trovaCartellaDist();

    private static File trovaCartellaDist() {
        File base = new File(".").getAbsoluteFile();
        File candidata;
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

                        
                        return indexHtml;
                    }
                });
    }
}
