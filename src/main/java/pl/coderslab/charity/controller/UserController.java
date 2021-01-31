package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CurrentUser;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        List<User> users = userService.findUsersByRole("ROLE_USER");
        model.addAttribute("user", currentUser.getUser());
        model.addAttribute("users", users);
        return "listUser";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Optional<User> optionalUser = userService.get(id);
        User user = optionalUser.orElse(null);
        if(user == null){
            return "problemAdmin";
        }

        model.addAttribute("userToEdit", user);
        return "editUser";
    }

    @PostMapping("/edit")
    public String processEdit(@Valid @ModelAttribute("userToEdit") User userToEdit, BindingResult result){
        if(userService.existsByEmail(userToEdit.getEmail())){
            Optional<User> optionalUser = userService.get(userToEdit.getId());
            User userFromDatabase = optionalUser.orElse(null);
            if(userFromDatabase == null){
                return "problemAdmin";
            }
            if(!userFromDatabase.getEmail().equals(userToEdit.getEmail())){ //checking if user is trying change email to email of another user
                FieldError error = new FieldError("userToEdit", "email", "Email już istnieje w bazie danych");
                result.addError(error);
            }

        }
        if(result.hasErrors()){
            return "editUser";
        }
        userService.editUser(userToEdit);
        return "redirect:/users/list";
    }

}
