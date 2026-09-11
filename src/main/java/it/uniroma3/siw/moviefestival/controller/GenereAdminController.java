package it.uniroma3.siw.moviefestival.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import it.uniroma3.siw.moviefestival.model.Genere;
import it.uniroma3.siw.moviefestival.service.GenereService;

@Controller
public class GenereAdminController {

    private final GenereService genereService;

    public GenereAdminController(GenereService genereService) {
        this.genereService = genereService;
    }

    @GetMapping("/admin/generi")
    public String elenco(Model model) {
        model.addAttribute("generi", genereService.findAll());
        return "admin/generi";
    }

    @GetMapping("/admin/generi/nuovo")
    public String formNuovo(Model model) {
        model.addAttribute("genere", new Genere());
        model.addAttribute("erroreValidazione", false);
        return "admin/genereForm";
    }

    @GetMapping("/admin/generi/{id}/modifica")
    public String formModifica(@PathVariable("id") Long id, Model model) {
        Genere genere = genereService.findById(id);
        if (genere == null) {
            return "redirect:/admin/generi";
        }
        model.addAttribute("genere", genere);
        model.addAttribute("erroreValidazione", false);
        return "admin/genereForm";
    }

    @PostMapping("/admin/generi")
    public String salva(@Valid @ModelAttribute("genere") Genere genereForm,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("erroreValidazione", true);
            return "admin/genereForm";
        }

        Genere genere;
        if (genereForm.getId() != null) {
            genere = genereService.findById(genereForm.getId());
        } else {
            genere = new Genere();
        }

        genere.setNome(genereForm.getNome());

        genereService.save(genere);
        return "redirect:/admin/generi";
    }

    @PostMapping("/admin/generi/{id}/elimina")
    public String elimina(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            genereService.deleteById(id);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erroreEliminazione", e.getMessage());
        }
        return "redirect:/admin/generi";
    }
}
