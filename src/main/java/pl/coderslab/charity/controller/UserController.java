package pl.coderslab.charity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.User;
import pl.coderslab.charity.service.CurrentUser;
import pl.coderslab.charity.service.DonationService;
import pl.coderslab.charity.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final DonationService donationService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder,
                          DonationService donationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.donationService = donationService;
    }

    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        Optional<User> optionalUser = userService.get(customUser.getUser().getId());
        User user = optionalUser.orElse(null);

        if (user == null) {
            return "problemAdmin";
        }

        model.addAttribute("user", user);
        model.addAttribute("userToEdit", user);
        return "editAdmin";
    }

    @PostMapping("/edit")
    public String processEdit(@Valid @ModelAttribute("userToEdit") User user, BindingResult result) {
        if (userService.existsByEmail(user.getEmail())) {
            Optional<User> optionalUser = userService.get(user.getId());
            User userFromDatabase = optionalUser.orElse(null);
            if (userFromDatabase == null) {
                return "problemAdmin";
            }
            if (!userFromDatabase.getEmail().equals(user.getEmail())) { //checking if user is trying change email to email of another user
                FieldError error = new FieldError("user", "email", "Email już istnieje w bazie danych");
                result.addError(error);
            }

        }
        if (result.hasErrors()) {
            return "editAdmin";
        }
        userService.editUser(user);
        return "redirect:/users/user/userPage";
    }

    @GetMapping("/editPassword")
    public String editPassword(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        Optional<User> optionalUser = userService.get(customUser.getUser().getId());
        User user = optionalUser.orElse(null);

        if (user == null) {
            return "problemAdmin";
        }

        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("userToEditPassword", user);
        return "editAdminPassword";
    }

    @PostMapping("/editPassword")
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
        return "redirect:/users/user/userPage";
    }

    @GetMapping("/userPage")
    public String sbAdmin2(@AuthenticationPrincipal CurrentUser customUser, Model model) {
        Optional<User> optionalUser = userService.get(customUser.getUser().getId());
        User user = optionalUser.orElse(null);

        if(user == null){
            return "problemAdmin";
        }


        model.addAttribute("user", user);
        return "userPage";
    }

    @GetMapping("/donationList")
    public String donationList(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        List<Donation> userDonations = donationService.findDonationsOfUser(currentUser.getUser());
        model.addAttribute("userDonations", userDonations);
        return "userDonations";
    }

    @GetMapping("/sortDonationsByTakenNotTaken")
    public String sortDonationByTakenNotTaken(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        List<Donation> taken = donationService.findTakenAndNotTaken(currentUser.getUser(), true);
        List<Donation> notTaken = donationService.findTakenAndNotTaken(currentUser.getUser(), false);
        List<Donation> takenNotTaken =
                Stream.concat(taken.stream(), notTaken.stream())
                .collect(Collectors.toList());
        model.addAttribute("userDonations", takenNotTaken);
        return "userDonations";
    }

    @GetMapping("/sortDonationsByTakenFromMeDate")
    public String sortDonationsByTakenFromMeDate(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        List<Donation> sortedByTakenFromMeDate = donationService.findAllSortedByTakenFromMeDateDesc(currentUser.getUser());
        model.addAttribute("userDonations", sortedByTakenFromMeDate);
        return "userDonations";
    }

    @GetMapping("/sortDonationsByCreationDate")
    public String sortDonationsByCreationDate(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        List<Donation> sortedByCreationDate = donationService.findAllSortedByCreationDateDesc(currentUser.getUser());
        model.addAttribute("userDonations", sortedByCreationDate);
        return "userDonations";
    }

    @GetMapping("/donationDetails/{id}")
    public String donationDetails(@PathVariable Long id, Model model){
        Optional<Donation> optionalDonation = donationService.get(id);
        Donation userDonation = optionalDonation.orElse(null);
        if(userDonation == null){
            return "problemDonation";
        }

        model.addAttribute("userDonation", userDonation);
        return "donationDetails";
    }

    @GetMapping("/giveDonation/{id}")
    public String giveDonation(@PathVariable Long id, Model model){
        Optional<Donation> optionalDonation = donationService.get(id);
        Donation donation = optionalDonation.orElse(null);
        if(donation == null){
            return "problemDonation";
        }

        model.addAttribute("donation", donation);
        return "changeStatusDonation";
    }

    @PostMapping("/giveDonation")
    public String giveDonationProcess(@Valid Donation donation, BindingResult result){

        System.out.println(donation.getTakenFromMeDateString());
        if(donation.getTakenFromMeDateString() == null){
            FieldError error  = new FieldError("donation", "takenFromMeDateString", "Nie wybrano daty");
            result.addError(error);
        }

        if (result.hasErrors()){
            return "changeStatusDonation";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        donation.setTakenFromMeDate(LocalDate.parse(donation.getTakenFromMeDateString(), formatter));

        donationService.update(donation);
        return "redirect:/users/donationDetails/" + donation.getId();
    }
}
