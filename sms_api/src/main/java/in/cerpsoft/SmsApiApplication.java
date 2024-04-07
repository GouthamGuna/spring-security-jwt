package in.cerpsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class SmsApiApplication {

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/cerp-sms");
        SpringApplication.run(SmsApiApplication.class, args);
    }
}
