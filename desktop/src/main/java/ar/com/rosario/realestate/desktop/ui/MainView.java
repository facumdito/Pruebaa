package ar.com.rosario.realestate.desktop.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {

    private MainView() {}

    public static void show(Stage stage, String userEmail, String tenantId) {
        BorderPane root = new BorderPane();

        VBox navRail = buildNavRail(root, userEmail, tenantId);
        navRail.setPrefWidth(200);
        root.setLeft(navRail);

        root.setCenter(DashboardView.build(tenantId));

        Scene scene = new Scene(root, 1280, 800);
        stage.setTitle("Rosario Real Estate — " + userEmail);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    private static VBox buildNavRail(BorderPane root, String userEmail, String tenantId) {
        Label appName = new Label("RRE");
        appName.getStyleClass().add("title-3");
        appName.setPadding(new Insets(16, 0, 24, 0));

        Button dashboard   = navBtn("Dashboard",   () -> root.setCenter(DashboardView.build(tenantId)));
        Button propiedades = navBtn("Propiedades", () -> root.setCenter(PropiedadesView.build(tenantId)));
        Button leads       = navBtn("Leads",       () -> root.setCenter(LeadsView.build(tenantId)));
        Button visitas     = navBtn("Visitas",      () -> root.setCenter(VisitasView.build(tenantId)));
        Button agentes     = navBtn("Agentes",      () -> root.setCenter(AgentesView.build(tenantId)));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label user = new Label(userEmail);
        user.getStyleClass().add("text-muted");
        user.setWrapText(true);
        user.setPadding(new Insets(8));

        VBox nav = new VBox(8, appName, dashboard, propiedades, leads, visitas, agentes, spacer, user);
        nav.setPadding(new Insets(12));
        nav.getStyleClass().add("card");
        return nav;
    }

    private static Button navBtn(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setOnAction(e -> action.run());
        return btn;
    }
}
