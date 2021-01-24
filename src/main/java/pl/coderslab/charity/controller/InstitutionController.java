package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.service.CurrentUser;
import pl.coderslab.charity.service.InstitutionService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping("/add")
    public String form(@AuthenticationPrincipal CurrentUser customUser, Model model){
        model.addAttribute("institution", new Institution());
        model.addAttribute("user", customUser.getUser());
        return "addInstitution";
    }

    @PostMapping("/add")
    public String processForm(@Valid Institution institution, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "addInstitution";
        }

        institutionService.save(institution);
        return "redirect:/users/sb-admin-2";
    }

    @GetMapping("/list")
    public String list(Model model){
        List<Institution> institutions = institutionService.getInstitutions();
        model.addAttribute("institutions", institutions);
        return "listInstitution";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser customUser){
        Optional<Institution> optionalInstitution = institutionService.get(id);
        Institution institution = optionalInstitution.orElse(null);
        if(institution == null){
            return "problemInstitution";
        }
        model.addAttribute("institution", institution);
        model.addAttribute("user", customUser.getUser());
        return "addInstitution";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal CurrentUser customUser){
        Optional<Institution> optionalInstitution = institutionService.get(id);
        Institution institution = optionalInstitution.orElse(null);
        if(institution == null){
            return "problemInstitution";
        }
        institutionService.delete(id);
        return "redirect:/institutions/list";
    }
}
