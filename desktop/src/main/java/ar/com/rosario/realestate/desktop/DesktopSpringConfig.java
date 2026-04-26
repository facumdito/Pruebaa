package ar.com.rosario.realestate.desktop;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Spring Boot root for desktop — no web server, no security filter chain.
 * Scans persistence module so JPA repositories are available to JavaFX views.
 */
@SpringBootApplication(
    scanBasePackages = "ar.com.rosario.realestate",
    exclude = { WebMvcAutoConfiguration.class, SecurityAutoConfiguration.class }
)
public class DesktopSpringConfig {}
