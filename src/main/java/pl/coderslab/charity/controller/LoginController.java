package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findMail")
    public String findMailForm(Model model){
        model.addAttribute("user", new User());
        return "forgottenPasswordForm";
    }

    @PostMapping("/findMail")
    public String findMail(@Valid User user, BindingResult bindingResult){

        if ("".equals(user.getEmail())){
            FieldError error = new FieldError("user", "email", "Podaj adres email");
            bindingResult.addError(error);
            return "forgottenPasswordForm";
        }

        User userFromDatabase = userService.findByEmail(user.getEmail());

        if (userFromDatabase == null){
            FieldError error = new FieldError("user", "email",
                    "Podany adres email nie istnieje w bazie danych");
            bindingResult.addError(error);
            return "forgottenPasswordForm";
        }

      /*  if (bindingResult.hasErrors()){
            return "forgottenPasswordForm";
        }*/

        return "index";

    }

}
