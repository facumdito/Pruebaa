package ar.com.rosario.realestate.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
    scanBasePackages = "ar.com.rosario.realestate.persistence",
    exclude = SecurityAutoConfiguration.class
)
public class PersistenceTestConfig {}
