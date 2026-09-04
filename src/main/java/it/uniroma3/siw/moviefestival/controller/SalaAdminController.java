package it.uniroma3.siw.moviefestival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.moviefestival.model.Sala;
import it.uniroma3.siw.moviefestival.service.SalaService;

@Controller
public class SalaAdminController {

    private final SalaService salaService;

    public SalaAdminController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping("/admin/sale")
    public String elenco(Model model) {
        model.addAttribute("sale", salaService.findAll());
        return "admin/sale";
    }

    @GetMapping("/admin/sale/nuova")
    public String formNuova(Model model) {
        model.addAttribute("sala", new Sala());
        return "admin/salaForm";
    }

    @GetMapping("/admin/sale/{id}/modifica")
    public String formModifica(@PathVariable("id") Long id, Model model) {
        Sala sala = salaService.findById(id);
        if (sala == null) {
            return "redirect:/admin/sale";
        }
        model.addAttribute("sala", sala);
        return "admin/salaForm";
    }

    @PostMapping("/admin/sale")
    public String salva(@ModelAttribute("id") Long id,
                         @ModelAttribute("nome") String nome,
                         @ModelAttribute("indirizzo") String indirizzo,
                         @ModelAttribute("capienza") Integer capienza) {

        Sala sala;
        if (id != null) {
            sala = salaService.findById(id);
        } else {
            sala = new Sala();
        }

        sala.setNome(nome);
        sala.setIndirizzo(indirizzo);
        sala.setCapienza(capienza);

        salaService.save(sala);
        return "redirect:/admin/sale";
    }

    @PostMapping("/admin/sale/{id}/elimina")
    public String elimina(@PathVariable("id") Long id) {
        salaService.deleteById(id);
        return "redirect:/admin/sale";
    }
}