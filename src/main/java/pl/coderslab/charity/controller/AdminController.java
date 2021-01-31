package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/admins")
public class AdminController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
        return "redirect:/admins/sb-admin-2";
    }

    @GetMapping("/list")
    public String list (@AuthenticationPrincipal CurrentUser customUser, Model model){
        List<User> admins = userService.findUsersByRole("ROLE_ADMIN");
        model.addAttribute("admins", admins);
        model.addAttribute("user", customUser.getUser());
        return "listAdmin";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Optional<User> optionalUser = userService.get(id);
        User admin = optionalUser.orElse(null);
        if(admin == null){
            return "problemAdmin";
        }
        model.addAttribute("admin", admin);
        return "editAdmin";
    }

    @PostMapping("/edit")
    public String processEdit(@Valid @ModelAttribute("admin") User admin, BindingResult result){
        if(userService.existsByEmail(admin.getEmail())){
            Optional<User> optionalUser = userService.get(admin.getId());
            User adminFromDatabase = optionalUser.orElse(null);
            if(adminFromDatabase == null){
                return "problemAdmin";
            }
            if(!adminFromDatabase.getEmail().equals(admin.getEmail())){ //checking if admin is trying change email to email of another admin
                FieldError error = new FieldError("admin", "email", "Email już istnieje w bazie danych");
                result.addError(error);
            }

        }
        if(result.hasErrors()){
            return "editAdmin";
        }
        userService.editAdmin(admin);
        return "redirect:/admins/list";
    }

    @GetMapping("/editPassword/{id}")
    public String editPassword(@PathVariable Long id, Model model){
        Optional<User> optionalUser = userService.get(id);
        User admin = optionalUser.orElse(null);
        if(admin == null){
            return "problemAdmin";
        }
        admin.setPassword("");
        model.addAttribute("admin", admin);
        return "editAdminPassword";
    }

    @PostMapping("/editPassword")
    public String processEditPassword(@Valid @ModelAttribute("admin") User admin, BindingResult result){

        Optional<User> optionalUser = userService.get(admin.getId());
        User adminFromDataBase = optionalUser.orElse(null);
        if(adminFromDataBase == null){
            return "problemAdmin";
        }

        String oldPasswordFromDataBase = adminFromDataBase.getPassword();
        String oldPasswordFromForm = admin.getOldPassword();

        if(!passwordEncoder.matches(oldPasswordFromForm, oldPasswordFromDataBase)){
            FieldError error = new FieldError("admin", "oldPassword", "Hasło nie jest poprawne");
            result.addError(error);
        }

        if(!admin.getPassword().equals(admin.getPassword2())){
            FieldError error = new FieldError("admin", "password2", "Hasła nie są takie same");
            result.addError(error);
        }

        if(result.hasErrors()){
            return "editAdminPassword";
        }
        userService.editAdminPassword(admin);
        return "redirect:/admins/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        Optional<User> optionalUser = userService.get(id);
        User admin = optionalUser.orElse(null);
        if(admin == null){
            return "problemAdmin";
        }

        userService.delete(id);
        return "redirect:/admins/list";
    }

}
