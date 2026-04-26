package ar.com.rosario.realestate.desktop.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Main shell: left nav-rail + content pane.
 * Screens: Dashboard, Propiedades, Leads, Visitas, Tasaciones, Agentes.
 */
public class MainView {

    private MainView() {}

    public static void show(Stage stage, String userEmail) {
        BorderPane root = new BorderPane();

        VBox navRail = buildNavRail(root, userEmail);
        navRail.setPrefWidth(200);
        root.setLeft(navRail);

        showDashboard(root);

        Scene scene = new Scene(root, 1200, 768);
        stage.setTitle("Rosario Real Estate — " + userEmail);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    private static VBox buildNavRail(BorderPane root, String userEmail) {
        Label appName = new Label("RRE");
        appName.getStyleClass().add("title-3");
        appName.setPadding(new Insets(16, 0, 24, 0));

        Button dashboard    = navBtn("Dashboard",     () -> showDashboard(root));
        Button propiedades  = navBtn("Propiedades",   () -> showPropiedades(root));
        Button leads        = navBtn("Leads",         () -> showLeads(root));
        Button visitas      = navBtn("Visitas",       () -> showVisitas(root));
        Button tasaciones   = navBtn("Tasaciones",    () -> showTasaciones(root));
        Button agentes      = navBtn("Agentes",       () -> showAgentes(root));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label user = new Label(userEmail);
        user.getStyleClass().add("text-muted");
        user.setWrapText(true);
        user.setPadding(new Insets(8));

        VBox nav = new VBox(8, appName, dashboard, propiedades, leads, visitas, tasaciones, agentes, spacer, user);
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

    private static void showDashboard(BorderPane root) {
        root.setCenter(DashboardView.build());
    }

    private static void showPropiedades(BorderPane root) {
        root.setCenter(PlaceholderView.build("Propiedades", "Listado, filtros y alta rápida. Fase 14."));
    }

    private static void showLeads(BorderPane root) {
        root.setCenter(PlaceholderView.build("Leads", "Pipeline BANT, importación WhatsApp. Fase 14."));
    }

    private static void showVisitas(BorderPane root) {
        root.setCenter(PlaceholderView.build("Visitas", "Calendario y gestión de visitas. Fase 15."));
    }

    private static void showTasaciones(BorderPane root) {
        root.setCenter(PlaceholderView.build("Tasaciones", "AVM + comparables + ficha PDF. Fase 15."));
    }

    private static void showAgentes(BorderPane root) {
        root.setCenter(PlaceholderView.build("Agentes", "Gestión de agentes y comisiones. Fase 16."));
    }
}
