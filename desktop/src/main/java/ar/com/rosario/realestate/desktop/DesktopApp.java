package ar.com.rosario.realestate.desktop;

import ar.com.rosario.realestate.desktop.ui.LoginView;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX 21 entry point. Starts headless Spring Boot context before showing UI,
 * so JPA repositories are available to all views.
 */
public class DesktopApp extends Application {

    @Override
    public void init() {
        AppContext.start();
    }

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        Scene scene = new Scene(LoginView.build(primaryStage), 420, 520);
        primaryStage.setTitle("Rosario Real Estate");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        AppContext.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
