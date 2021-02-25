package pl.coderslab.charity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.coderslab.charity.interfaces.EmailService;
import pl.coderslab.charity.service.EmailServiceImpl;

@Controller
public class TestController {

    @Autowired
    public EmailService emailService;


   /* @GetMapping("/sendMail")
        public String sendMail(){
            emailService.sendSimpleMessage("kubikmmarcin@gmail.com",
                    "subject", "text");
            return "index";
        }*/

}
