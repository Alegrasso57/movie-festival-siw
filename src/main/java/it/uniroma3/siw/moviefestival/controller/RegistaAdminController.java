package it.uniroma3.siw.moviefestival.controller;

import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.moviefestival.model.Regista;
import it.uniroma3.siw.moviefestival.service.RegistaService;

@Controller
public class RegistaAdminController {

    private final RegistaService registaService;

    public RegistaAdminController(RegistaService registaService) {
        this.registaService = registaService;
    }

    @GetMapping("/admin/registi")
    public String elenco(Model model) {
        model.addAttribute("registi", registaService.findAll());
        return "admin/registi";
    }

    @GetMapping("/admin/registi/nuovo")
    public String formNuovo(Model model) {
        model.addAttribute("regista", new Regista());
        return "admin/registaForm";
    }

    @GetMapping("/admin/registi/{id}/modifica")
    public String formModifica(@PathVariable("id") Long id, Model model) {
        Regista regista = registaService.findById(id);
        if (regista == null) {
            return "redirect:/admin/registi";
        }
        model.addAttribute("regista", regista);
        return "admin/registaForm";
    }

    @PostMapping("/admin/registi")
    public String salva(@ModelAttribute("id") Long id,
                         @ModelAttribute("nome") String nome,
                         @ModelAttribute("cognome") String cognome,
                         @ModelAttribute("dataNascita") String dataNascita,
                         @ModelAttribute("nazionalita") String nazionalita) {

        Regista regista;
        if (id != null) {
            regista = registaService.findById(id);
        } else {
            regista = new Regista();
        }

        regista.setNome(nome);
        regista.setCognome(cognome);
        regista.setDataNascita(LocalDate.parse(dataNascita));
        regista.setNazionalita(nazionalita);

        registaService.save(regista);
        return "redirect:/admin/registi";
    }

    @PostMapping("/admin/registi/{id}/elimina")
    public String elimina(@PathVariable("id") Long id) {
        registaService.deleteById(id);
        return "redirect:/admin/registi";
    }
}