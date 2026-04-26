package ar.com.rosario.realestate.desktop.ui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * Dashboard screen: KPI tiles — propiedades activas, leads nuevos, visitas pendientes, última tasación.
 * Phase 16: wire KPI tiles to live repository counts via async Task.
 */
public class DashboardView {

    private DashboardView() {}

    public static Parent build() {
        Label title = new Label("Dashboard");
        title.getStyleClass().add("title-2");

        HBox tiles = new HBox(16,
            kpiTile("Propiedades activas", "—"),
            kpiTile("Leads nuevos (7d)",   "—"),
            kpiTile("Visitas pendientes",  "—"),
            kpiTile("Últ. tasación USD",   "—")
        );
        tiles.setPadding(new Insets(0, 0, 24, 0));

        Label recent = new Label("Actividad reciente");
        recent.getStyleClass().add("title-4");

        Label placeholder = new Label("Sin actividad registrada.");
        placeholder.getStyleClass().add("text-muted");

        VBox root = new VBox(16, title, tiles, recent, placeholder);
        root.setPadding(new Insets(24));
        return root;
    }

    private static VBox kpiTile(String caption, String value) {
        Label val = new Label(value);
        val.getStyleClass().add("title-1");

        Label cap = new Label(caption);
        cap.getStyleClass().add("text-muted");

        VBox tile = new VBox(4, val, cap);
        tile.setPadding(new Insets(16));
        tile.setPrefWidth(220);
        tile.getStyleClass().add("card");
        HBox.setHgrow(tile, Priority.ALWAYS);
        return tile;
    }
}
