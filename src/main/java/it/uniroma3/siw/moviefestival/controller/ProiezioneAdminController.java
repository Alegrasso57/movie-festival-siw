package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.LocalTime;
import it.uniroma3.siw.moviefestival.model.Proiezione;
import it.uniroma3.siw.moviefestival.model.StatoProiezione;
import it.uniroma3.siw.moviefestival.service.FestivalService;
import it.uniroma3.siw.moviefestival.service.FilmService;
import it.uniroma3.siw.moviefestival.service.ProiezioneService;
import it.uniroma3.siw.moviefestival.service.SalaService;

@Controller
public class ProiezioneAdminController {

    private final ProiezioneService proiezioneService;
    private final FestivalService festivalService;
    private final FilmService filmService;
    private final SalaService salaService;

    public ProiezioneAdminController(ProiezioneService proiezioneService,
                                      FestivalService festivalService,
                                      FilmService filmService,
                                      SalaService salaService) {
        this.proiezioneService = proiezioneService;
        this.festivalService = festivalService;
        this.filmService = filmService;
        this.salaService = salaService;
    }

    @GetMapping("/admin/proiezioni")
    public String elenco(Model model) {
        model.addAttribute("proiezioniList", proiezioneService.findAll());
        return "admin/proiezioni";
    }

    @GetMapping("/admin/proiezioni/nuova")
    public String formNuova(Model model) {
        caricaListeSelezione(model);
        model.addAttribute("proiezione", null);
        model.addAttribute("errore", null);
        return "admin/proiezioneForm";
    }

    @GetMapping("/admin/proiezioni/{id}/modifica")
    public String formModifica(@PathVariable("id") Long id, Model model) {
        Proiezione proiezione = proiezioneService.findById(id);
        if (proiezione == null) {
            return "redirect:/admin/proiezioni";
        }
        caricaListeSelezione(model);
        model.addAttribute("proiezione", proiezione);
        model.addAttribute("errore", null);
        return "admin/proiezioneForm";
    }

    @PostMapping("/admin/proiezioni")
    public String salva(@RequestParam(value = "id", required = false) Long id,
                         @ModelAttribute("festivalId") Long festivalId,
                         @ModelAttribute("filmId") Long filmId,
                         @ModelAttribute("salaId") Long salaId,
                         @ModelAttribute("data") String data,
                         @ModelAttribute("ora") String ora,
                         Model model) {

        if (data == null || data.isBlank()) {
            return riformaConErrore(model, id, "La data è obbligatoria");
        }
        if (ora == null || ora.isBlank()) {
            return riformaConErrore(model, id, "L'ora è obbligatoria");
        }

        try {
            LocalDate dataParsata = LocalDate.parse(data);
            LocalTime oraParsata = LocalTime.parse(ora);

            if (id == null) {
                proiezioneService.creaProiezione(festivalId, filmId, salaId, dataParsata, oraParsata);
            } else {
                proiezioneService.modificaProiezione(id, festivalId, filmId, salaId, dataParsata, oraParsata);
            }
            return "redirect:/admin/proiezioni";

        } catch (IllegalArgumentException | IllegalStateException e) {
            return riformaConErrore(model, id, e.getMessage());
        }
    }

    @PostMapping("/admin/proiezioni/{id}/elimina")
    public String elimina(@PathVariable("id") Long id) {
        proiezioneService.deleteById(id);
        return "redirect:/admin/proiezioni";
    }

    @PostMapping("/admin/proiezioni/{id}/completa")
    public String segnaCompletata(@PathVariable("id") Long id) {
        Proiezione proiezione = proiezioneService.findById(id);
        if (proiezione != null) {
            proiezione.setStato(StatoProiezione.COMPLETED);
            proiezioneService.save(proiezione);
        }
        return "redirect:/admin/proiezioni";
    }

    private String riformaConErrore(Model model, Long id, String messaggio) {
        caricaListeSelezione(model);
        model.addAttribute("proiezione", id != null ? proiezioneService.findById(id) : null);
        model.addAttribute("errore", messaggio);
        return "admin/proiezioneForm";
    }

    private void caricaListeSelezione(Model model) {
        model.addAttribute("festivalDisponibili", festivalService.findAll());
        model.addAttribute("filmDisponibili", filmService.findAll());
        model.addAttribute("saleDisponibili", salaService.findAll());
    }
}
