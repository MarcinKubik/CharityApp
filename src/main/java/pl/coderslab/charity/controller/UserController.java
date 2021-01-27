package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.entity.Role;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CurrentUser;
import pl.coderslab.charity.service.RoleService;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegisterForm(@Valid User user, BindingResult result){
        if(userService.existsByEmail(user.getEmail())){
            result.rejectValue("email","Email już istnieje w bazie danych", "Email już istnieje w bazie danych");
        }
        if(user.getPassword2() != null && !user.getPassword2().equals(user.getPassword())){
            result.rejectValue("password2", "Hasła nie są takie same", "Hasła nie są takie same");
        }
        if(result.hasErrors()){
            return "register";
        }
        userService.saveUser(user);
        return "index";
    }

    @GetMapping("/sb-admin-2")
    public String sbAdmin2(@AuthenticationPrincipal CurrentUser customUser, Model model){
        model.addAttribute("user", customUser.getUser());
        return "sb-admin-2";
    }


    @GetMapping("/add")
    public String addAdmin(@AuthenticationPrincipal CurrentUser customUser, Model model){
        model.addAttribute("admin", new User());
        model.addAttribute("user", customUser.getUser());
        return "addAdmin";
    }

    @PostMapping("/add")
    public String processAddAdmin(@Valid @ModelAttribute("admin") User admin, BindingResult result){
        if((admin.getId() == null) && userService.existsByEmail(admin.getEmail())){  //first expression is necessary to set the same email during edition
            result.rejectValue("email","Email już istnieje w bazie danych", "Email już istnieje w bazie danych");

        }
        if(admin.getPassword2() != null && !admin.getPassword2().equals(admin.getPassword())){
            result.rejectValue("password2", "Hasła nie są takie same", "Hasła nie są takie same");
        }
        if(result.hasErrors()){
            return "addAdmin";
        }

        userService.saveAdmin(admin);
        return "redirect:/users/sb-admin-2";
    }

    @GetMapping("/list")
    public String list (Model model){
        List<User> admins = userService.findAdmins("ROLE_ADMIN");
        model.addAttribute("admins", admins);
        return "listAdmin";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Optional<User> optionalUser = userService.get(id);
        User admin = optionalUser.orElse(null);
        if(admin == null){
            return "problemAdmin";
        }
        admin.setPassword(null);
        model.addAttribute("admin", admin);
        return "editAdmin";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        Optional<User> optionalUser = userService.get(id);
        User admin = optionalUser.orElse(null);
        if(admin == null){
            return "problemAdmin";
        }

        userService.delete(id);
        return "redirect:/users/list";
    }

}
