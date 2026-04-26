package ar.com.rosario.realestate.desktop.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Login screen. Phase 13: local credential check.
 * Phase 15+: full OAuth2 PKCE flow via system browser + loopback redirect.
 */
public class LoginView {

    private LoginView() {}

    public static Parent build(Stage stage) {
        Label title = new Label("Rosario Real Estate");
        title.getStyleClass().add("title-1");

        Label subtitle = new Label("Ingresá tus credenciales");
        subtitle.getStyleClass().add("text-muted");

        TextField email = new TextField();
        email.setPromptText("Email");
        email.setMaxWidth(320);

        PasswordField password = new PasswordField();
        password.setPromptText("Contraseña");
        password.setMaxWidth(320);

        Button loginBtn = new Button("Ingresar");
        loginBtn.setDefaultButton(true);
        loginBtn.setMaxWidth(320);
        loginBtn.getStyleClass().add("accent");
        loginBtn.setOnAction(e -> onLogin(email.getText(), password.getText(), stage));

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("danger");

        VBox root = new VBox(12, title, subtitle, email, password, loginBtn, errorLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        return root;
    }

    private static void onLogin(String email, String password, Stage stage) {
        // Phase 15: replace with PKCE token exchange
        if (!email.isBlank() && !password.isBlank()) {
            MainView.show(stage, email);
        }
    }
}
