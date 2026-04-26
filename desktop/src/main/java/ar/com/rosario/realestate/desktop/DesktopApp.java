package ar.com.rosario.realestate.desktop;

import ar.com.rosario.realestate.desktop.ui.LoginView;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX 21 desktop application entry point.
 * Theme: AtlantaFX PrimerLight. Auth: OAuth2 PKCE against cloud backend.
 * All reads/writes go to local MySQL; SyncManager drains outbox in background.
 */
public class DesktopApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        Scene scene = new Scene(LoginView.build(primaryStage), 420, 520);
        primaryStage.setTitle("Rosario Real Estate");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
