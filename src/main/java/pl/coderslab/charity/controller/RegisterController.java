package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.interfaces.EmailService;
import pl.coderslab.charity.service.CurrentUser;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
public class RegisterController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmailService emailService;

    public RegisterController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegisterForm(@Valid User user, BindingResult result) {
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "Email już istnieje w bazie danych", "Email już istnieje w bazie danych");
        }
        if (user.getPassword2() != null && !user.getPassword2().equals(user.getPassword())) {
            result.rejectValue("password2", "Hasła nie są takie same", "Hasła nie są takie same");
        }
        if (result.hasErrors()) {
            return "register";
        }

        UUID uuid = UUID.randomUUID();
        user.setToken(uuid.toString());
        emailService.sendSimpleMessage(user.getEmail(), "Rejestracja w Charity App",
                "Witaj" + user.getFullName() + "<br>" + "Potwierdź aktywację konta klikając w poniższy link " + "<br>"
                        + "<a href=/confirmAccountActivation/" + user.getEmail() + "/" + user.getToken()
                        + ">Potwierdź</a>");
        userService.saveUser(user);
        return "index";
    }

    @GetMapping("/confirmAccountActivation/{email}/{token}")
    public String cofirmAccountActivation(@PathVariable String token, @PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return "problemAdmin";
        }

        if (user.getToken().equals(token)) {
            userService.unblock(user);
        }
        return "index";
    }


}
