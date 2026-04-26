package ar.com.rosario.realestate.desktop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Bootstraps a headless Spring Boot context so JavaFX views can obtain JPA repositories.
 * Call {@code AppContext.start()} before launching JavaFX; call {@code close()} on shutdown.
 */
public class AppContext {

    private static ConfigurableApplicationContext ctx;

    public static void start() {
        if (ctx != null) return;
        SpringApplication app = new SpringApplication(DesktopSpringConfig.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.setHeadless(false);
        ctx = app.run();
    }

    public static <T> T get(Class<T> type) {
        if (ctx == null) throw new IllegalStateException("AppContext not started");
        return ctx.getBean(type);
    }

    public static void close() {
        if (ctx != null) ctx.close();
    }
}
