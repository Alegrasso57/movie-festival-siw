package it.uniroma3.siw.moviefestival.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("erroreValidazione", false);
        return "admin/registaForm";
    }

    @GetMapping("/admin/registi/{id}/modifica")
    public String formModifica(@PathVariable("id") Long id, Model model) {
        Regista regista = registaService.findById(id);
        if (regista == null) {
            return "redirect:/admin/registi";
        }
        model.addAttribute("regista", regista);
        model.addAttribute("erroreValidazione", false);
        return "admin/registaForm";
    }

    @PostMapping("/admin/registi")
    public String salva(@Valid @ModelAttribute("regista") Regista registaForm,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("erroreValidazione", true);
            return "admin/registaForm";
        }

        Regista regista;
        if (registaForm.getId() != null) {
            regista = registaService.findById(registaForm.getId());
        } else {
            regista = new Regista();
        }

        regista.setNome(registaForm.getNome());
        regista.setCognome(registaForm.getCognome());
        regista.setDataNascita(registaForm.getDataNascita());
        regista.setNazionalita(registaForm.getNazionalita());

        registaService.save(regista);
        return "redirect:/admin/registi";
    }

    @PostMapping("/admin/registi/{id}/elimina")
    public String elimina(@PathVariable("id") Long id) {
        registaService.deleteById(id);
        return "redirect:/admin/registi";
    }
}