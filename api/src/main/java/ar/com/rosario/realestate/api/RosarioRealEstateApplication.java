package ar.com.rosario.realestate.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "ar.com.rosario.realestate")
@EnableScheduling
public class RosarioRealEstateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RosarioRealEstateApplication.class, args);
    }
}
