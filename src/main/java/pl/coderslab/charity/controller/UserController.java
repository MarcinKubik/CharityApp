package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entity.Role;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CurrentUser;
import pl.coderslab.charity.service.RoleService;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
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
        if(!user.getPassword2().equals(user.getPassword())){
            FieldError error = new FieldError("user", "password2", "Hasła nie są takie same");
            result.addError(error);
        }
        if(result.hasErrors()){
            return "register";
        }
        userService.save(user);
        return "index";
    }

    @GetMapping("/sb-admin-2")
    public String sbAdmin2(@AuthenticationPrincipal CurrentUser customUser, Model model){
        model.addAttribute("user", customUser.getUser());
        return "sb-admin-2";
    }

}
