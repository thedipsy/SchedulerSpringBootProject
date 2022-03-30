package mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface;

public interface EmailService {

    void sendEmail(String to, String topic, String body);

}

