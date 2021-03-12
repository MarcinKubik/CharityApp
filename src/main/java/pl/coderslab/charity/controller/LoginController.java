package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.interfaces.EmailService;
import pl.coderslab.charity.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public EmailService emailService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findMail")
    public String findMailForm(Model model) {
        model.addAttribute("user", new User());
        return "forgottenPasswordForm";
    }

    @PostMapping("/findMail")
    public String findMail(@Valid User user, BindingResult bindingResult) {

        if ("".equals(user.getEmail())) {
            FieldError error = new FieldError("user", "email", "Podaj adres email");
            bindingResult.addError(error);
            return "forgottenPasswordForm";
        }

        User userFromDatabase = userService.findByEmail(user.getEmail());

        if (userFromDatabase == null) {
            FieldError error = new FieldError("user", "email",
                    "Podany adres email nie istnieje w bazie danych");
            bindingResult.addError(error);
            return "forgottenPasswordForm";
        }

        UUID uuid = UUID.randomUUID();
        userFromDatabase.setToken(uuid.toString());
        userService.editUser(userFromDatabase);
        emailService.sendSimpleMessage(userFromDatabase.getEmail(), "Resetowanie hasła", "Witaj " + userFromDatabase.getFullName()
                + ". Kliknij w link aby zresetować hasło "
                + "http://localhost:8080/resetPassword/" + userFromDatabase.getEmail() + "/" + userFromDatabase.getToken());
        return "index";
    }

    @GetMapping("/resetPassword/{email}/{token}")
    public String resetPassword(@PathVariable String email, @PathVariable String token, Model model) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return "problemAdmin";
        }

        if (token.equals(user.getToken())) {
            user.setPassword("");
            model.addAttribute("userToEditPassword", user);
            return "editPasswordAfterReset";
        }

        return "index";
    }

    @PostMapping("/resetPassword/{email}/{token}")
    public String processResetPassword(@Valid @ModelAttribute("userToEditPassword") User user, BindingResult result) {
        if (!user.getPassword().equals(user.getPassword2())) {
            FieldError error = new FieldError("user", "password2", "Hasła nie są takie same");
            result.addError(error);
        }

        if (result.hasErrors()) {
            return "editPasswordAfterReset";
        }

        UUID uuid = UUID.randomUUID();
        user.setToken(uuid.toString()); // setting random token to disable user change password by entrance with mail

        userService.editUserPassword(user);

        return "index";
    }
}
