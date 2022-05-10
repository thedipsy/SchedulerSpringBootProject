package mk.ukim.finki.wp.schedulerspringbootproject.Service.Implementation;

import mk.ukim.finki.wp.schedulerspringbootproject.Config.Constants;
import mk.ukim.finki.wp.schedulerspringbootproject.Service.Interface.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sending a mail using JavaMailSender
     * Purpose is to inform the employee of some changes that affects them
     * Mail is send from our email account
     */
    public void sendEmail(String to, String topic, String body){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String fromEmail = Constants.EMAIL_FROM;
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(topic);
        simpleMailMessage.setText(body);
        javaMailSender.send(simpleMailMessage);
    }
}
