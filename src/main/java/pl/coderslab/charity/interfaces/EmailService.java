package pl.coderslab.charity.interfaces;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
