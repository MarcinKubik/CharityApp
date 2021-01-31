package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CurrentUser;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
}
