package pl.coderslab.charity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.coderslab.charity.interfaces.EmailService;

@Component
public class EmailServiceImpl implements EmailService {

   // private static final String NOREPLY_ADDRESS = "mail@gmail.com";
    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text){

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        }catch (MailException exception){
            exception.printStackTrace();
        }

    }
}
