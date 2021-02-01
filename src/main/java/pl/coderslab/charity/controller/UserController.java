package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CurrentUser;
import pl.coderslab.charity.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/users/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal CurrentUser customUser, Model model){
        Optional<User> optionalUser = userService.get(customUser.getUser().getId());
        User user = optionalUser.orElse(null);

        if(user == null){
            return "problemAdmin";
        }

        return "editUser";
    }

    @GetMapping("/userPage")
    public String sbAdmin2(@AuthenticationPrincipal CurrentUser customUser, Model model){
        model.addAttribute("user", customUser.getUser());
        return "userPage";
    }
}
