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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class RegisterController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String UPPER_CASE_REGEX = "[A-Z]+";
    private static final String LOWER_CASE_REGEX = "[a-z]+";
    private static final String DIGIT_REGEX = "[0-9]+";
    private static final String SPECIAL_CHARACTER_REGEX = "\\W+";

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
        Pattern upperCasePattern = Pattern.compile(UPPER_CASE_REGEX);
        Matcher upperCaseMatcher = upperCasePattern.matcher(user.getPassword());
        if(!upperCaseMatcher.find()){
            result.rejectValue("password", "Hasło musi zawierać co najmniej jedną wielką literę", "Hasło musi zawierać co najmniej jedną wielką literę");
        }
        Pattern lowerCasePattern = Pattern.compile(LOWER_CASE_REGEX);
        Matcher lowerCaseMatcher = lowerCasePattern.matcher(user.getPassword());
        if(!lowerCaseMatcher.find()){
            result.rejectValue("password", "Hasło musi zawierać co najmniej jedną małą literę", "Hasło musi zawierać co najmniej jedną małą literę");
        }
        Pattern digitPattern = Pattern.compile(DIGIT_REGEX);
        Matcher digitMatcher = digitPattern.matcher(user.getPassword());
        if(!digitMatcher.find()){
            result.rejectValue("password", "Hasło musi zawierać co najmniej jedną cyfrę", "Hasło musi zawierać co najmniej jedną cyfrę");
        }
        Pattern specialCharacterPattern = Pattern.compile(SPECIAL_CHARACTER_REGEX);
        Matcher specialCharacterMatcher = specialCharacterPattern.matcher(user.getPassword());
        if(!specialCharacterMatcher.find()){
            result.rejectValue("password", "Hasło musi zawierać co najmniej jeden znak specjalny", "Hasło musi zawierać co najmniej jeden znak specjalny");
        }
        if (user.getPassword2() != null && !user.getPassword2().equals(user.getPassword())) {
            result.rejectValue("password2", "Hasła nie są takie same", "Hasła nie są takie same");
        }
        if (result.hasErrors()) {
            return "register";
        }

        UUID uuid = UUID.randomUUID();
        user.setToken(uuid.toString());
        emailService.sendSimpleMessage(user.getEmail(), "Rejestracja w Charity App", "Witaj " + user.getFullName() + ". Potwierdź aktywację konta klikając w link "
        + "http://localhost:8080/confirmAccountActivation/" + user.getEmail() + "/" + user.getToken());
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
            UUID uuid = UUID.randomUUID();
            user.setToken(uuid.toString()); // setting random token to disable user activation account after blocking by admin
            userService.unblock(user);
        }
        return "index";
    }


}
