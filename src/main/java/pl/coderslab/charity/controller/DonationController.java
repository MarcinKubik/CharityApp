package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.service.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class DonationController {

    private final CategoryService categoryService;
    private final InstitutionService institutionService;
    private final DonationService donationService;
    private final UserService userService;

    public DonationController(CategoryService categoryService, InstitutionService institutionService, DonationService donationService,
                              UserService userService) {
        this.categoryService = categoryService;
        this.institutionService = institutionService;
        this.donationService = donationService;
        this.userService = userService;
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryService.getCategories();
    }

    @ModelAttribute("institutions")
    public List<Institution> institutions() {
        return institutionService.getInstitutions();
    }

    @GetMapping("/form")
    public String form(Model model, @AuthenticationPrincipal CurrentUser customUser){
        model.addAttribute("donation", new Donation());
        model.addAttribute("user", customUser.getUser());

            return "form";
    }

    @PostMapping("/form")
    public String processForm(@Valid Donation donation, BindingResult result){
        if(result.hasErrors()){
            return "form";
        }

            donationService.save(donation);
            return "form-confirmation";
    }
}
