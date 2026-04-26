package ar.com.rosario.realestate.desktop.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    private LoginView() {}

    public static Parent build(Stage stage) {
        Label title = new Label("Rosario Real Estate");
        title.getStyleClass().add("title-1");

        Label subtitle = new Label("Ingresá tus credenciales");
        subtitle.getStyleClass().add("text-muted");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(320);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.setMaxWidth(320);

        // tenant field for local dev; in prod derives from JWT claim
        TextField tenantField = new TextField();
        tenantField.setPromptText("Tenant ID");
        tenantField.setMaxWidth(320);
        tenantField.setText("tenant-local");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("danger");

        Button loginBtn = new Button("Ingresar");
        loginBtn.setDefaultButton(true);
        loginBtn.setMaxWidth(320);
        loginBtn.getStyleClass().add("accent");
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String tenant = tenantField.getText().trim();
            if (email.isBlank() || passwordField.getText().isBlank()) {
                errorLabel.setText("Completá todos los campos.");
                return;
            }
            // Phase 15: replace with real OAuth2 PKCE token exchange
            MainView.show(stage, email, tenant);
        });

        VBox root = new VBox(12, title, subtitle, emailField, passwordField, tenantField, loginBtn, errorLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        return root;
    }
}
