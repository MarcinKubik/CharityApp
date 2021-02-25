package pl.coderslab.charity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.coderslab.charity.interfaces.EmailService;

@Controller
public class MailController {

    @Autowired
    public EmailService emailService;

    @GetMapping("/sendMail")
    public String sendMail(){
        emailService.sendSimpleMessage("mail@gmail.com",
                "subject", "text");


        return "index";
    }



}
