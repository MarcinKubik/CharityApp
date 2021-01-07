package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.service.CategoryService;
import pl.coderslab.charity.service.InstitutionService;

import java.util.List;

@Controller
public class DonationController {

    private final CategoryService categoryService;
    private final InstitutionService institutionService;

    public DonationController(CategoryService categoryService, InstitutionService institutionService) {
        this.categoryService = categoryService;
        this.institutionService = institutionService;
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
    public String form(Model model){
        model.addAttribute("donation", new Donation());
        return "form";
    }
}
