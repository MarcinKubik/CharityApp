package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
}
