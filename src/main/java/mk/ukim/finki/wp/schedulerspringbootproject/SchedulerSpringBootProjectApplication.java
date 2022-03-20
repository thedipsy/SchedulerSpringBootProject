package mk.ukim.finki.wp.schedulerspringbootproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SchedulerSpringBootProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerSpringBootProjectApplication.class, args);
    }

}
