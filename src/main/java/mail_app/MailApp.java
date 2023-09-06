package mail_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Locale;

@SpringBootApplication
public class MailApp {
    public static void main(String[] args) {
        SpringApplication.run(MailApp.class, args);
        Locale.setDefault(Locale.ENGLISH);
    }

}
