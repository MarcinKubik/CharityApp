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
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
        return "editAdmin";
    }

    @PostMapping("/edit/{id}")
    public String processEdit(@Valid @ModelAttribute("userToEdit") User user, BindingResult result){
        if(userService.existsByEmail(user.getEmail())){
            Optional<User> optionalUser = userService.get(user.getId());
            User userFromDatabase = optionalUser.orElse(null);
            if(userFromDatabase == null){
                return "problemAdmin";
            }
            if(!userFromDatabase.getEmail().equals(user.getEmail())){ //checking if user is trying change email to email of another user
                FieldError error = new FieldError("user", "email", "Email już istnieje w bazie danych");
                result.addError(error);
            }

        }
        if(result.hasErrors()){
            return "editAdmin";
        }
        userService.editUser(user);
        return "redirect:/users/list";
    }

    @GetMapping("/editPassword/{id}")
    public String editPassword(@PathVariable Long id, Model model){
        Optional<User> optionalUser = userService.get(id);
        User user = optionalUser.orElse(null);
        if(user == null){
            return "problemAdmin";
        }
        user.setPassword("");
        model.addAttribute("userToEditPassword", user);
        return "editAdminPassword";
    }

    @PostMapping("/editPassword/{id}")
    public String processEditPassword(@Valid @ModelAttribute("userToEditPassword") User user, BindingResult result){

        Optional<User> optionalUser = userService.get(user.getId());
        User userFromDataBase = optionalUser.orElse(null);
        if(userFromDataBase == null){
            return "problemAdmin";
        }

        String oldPasswordFromDataBase = userFromDataBase.getPassword();
        String oldPasswordFromForm = user.getOldPassword();

        if(!passwordEncoder.matches(oldPasswordFromForm, oldPasswordFromDataBase)){
            FieldError error = new FieldError("user", "oldPassword", "Hasło nie jest poprawne");
            result.addError(error);
        }

        if(!user.getPassword().equals(user.getPassword2())){
            FieldError error = new FieldError("user", "password2", "Hasła nie są takie same");
            result.addError(error);
        }

        if(result.hasErrors()){
            return "editAdminPassword";
        }
        userService.editUserPassword(user);
        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        Optional<User> optionalUser = userService.get(id);
        User user = optionalUser.orElse(null);
        if(user == null){
            return "problemAdmin";
        }

        userService.delete(id);
        return "redirect:/users/list";
    }

    @GetMapping("/block/{id}")
    public String blockUser(@PathVariable Long id){
        Optional<User> optionalUser = userService.get(id);
        User user = optionalUser.orElse(null);
        if(user == null){
            return "problemAdmin";
        }

        userService.block(user);
        return "redirect:/users/list";
    }
}
