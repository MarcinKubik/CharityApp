package pl.coderslab.charity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.entity.Role;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.InstitutionService;
import pl.coderslab.charity.service.RoleService;

import java.util.List;


@Controller
public class HomeController {

    private final InstitutionService institutionService;
    private final DonationService donationService;
    private final RoleService roleService;
    public HomeController(InstitutionService institutionService, DonationService donationService, RoleService roleService) {
        this.institutionService = institutionService;
        this.donationService = donationService;
        this.roleService = roleService;
    }

    @RequestMapping("/")
    public String homeAction(Model model){
        //adding roles if dont exist
        List<Role> roles = roleService.getRoles();
        if(roles.size() == 0){
            Role roleAdmin = new Role();
            roleAdmin.setId(Long.valueOf(1));
            roleAdmin.setName("ROLE_ADMIN");
            Role roleUser = new Role();
            roleUser.setId(Long.valueOf(2));
            roleUser.setName("ROLE_USER");
            roleService.save(roleAdmin);
            roleService.save(roleUser);
        }
        List<Institution> institutions = institutionService.getInstitutions();
        Integer bags = donationService.countBags();
        long allDonations = donationService.AllDonations();
        model.addAttribute("bags", bags);
        model.addAttribute("allDonations", allDonations);
        model.addAttribute("institutions", institutions);
        return "index";
    }
}
