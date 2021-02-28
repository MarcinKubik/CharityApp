package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.charity.interfaces.EmailService;



@Controller
@RequestMapping("/account")
public class AccountActivationController {

    @Autowired
    public EmailService emailService;

    @GetMapping("/sendNotification")
    public String sendMail(){
        emailService.sendSimpleMessage("mail@gmail.com",
                "subject", "text");


        return "index";
    }



}
