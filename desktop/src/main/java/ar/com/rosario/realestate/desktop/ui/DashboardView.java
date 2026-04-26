package ar.com.rosario.realestate.desktop.ui;

import ar.com.rosario.realestate.core.domain.EstadoPropiedad;
import ar.com.rosario.realestate.core.domain.EstadoVisita;
import ar.com.rosario.realestate.core.port.out.AgenteRepository;
import ar.com.rosario.realestate.core.port.out.LeadRepository;
import ar.com.rosario.realestate.core.port.out.PropiedadRepository;
import ar.com.rosario.realestate.core.port.out.VisitaRepository;
import ar.com.rosario.realestate.desktop.AppContext;
import ar.com.rosario.realestate.shared.TenantContext;
import ar.com.rosario.realestate.shared.TenantId;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class DashboardView {

    private DashboardView() {}

    public static Parent build(String tenantId) {
        Label title = new Label("Dashboard");
        title.getStyleClass().add("title-2");

        Label propVal   = kpiValue("—");
        Label leadsVal  = kpiValue("—");
        Label visitasVal = kpiValue("—");
        Label agentesVal = kpiValue("—");

        HBox tiles = new HBox(16,
            kpiTile("Propiedades disponibles", propVal),
            kpiTile("Leads activos",           leadsVal),
            kpiTile("Visitas pendientes",       visitasVal),
            kpiTile("Agentes activos",          agentesVal)
        );
        tiles.setPadding(new Insets(0, 0, 20, 0));

        VBox root = new VBox(16, title, tiles);
        root.setPadding(new Insets(24));

        Thread.ofVirtual().start(() -> {
            try {
                TenantContext.set(tenantId);
                TenantId tid = TenantId.of(tenantId);
                int props    = AppContext.get(PropiedadRepository.class)
                                         .findByTenantAndEstado(tid, EstadoPropiedad.DISPONIBLE).size();
                int leads    = AppContext.get(LeadRepository.class).findByTenant(tid).size();
                int visitas  = AppContext.get(VisitaRepository.class)
                                         .findByTenantAndEstado(tid, EstadoVisita.AGENDADA).size();
                int agentes  = AppContext.get(AgenteRepository.class).findActivosByTenant(tid).size();
                Platform.runLater(() -> {
                    propVal.setText(String.valueOf(props));
                    leadsVal.setText(String.valueOf(leads));
                    visitasVal.setText(String.valueOf(visitas));
                    agentesVal.setText(String.valueOf(agentes));
                });
            } catch (Exception e) {
                Platform.runLater(() -> propVal.setText("error"));
            } finally {
                TenantContext.clear();
            }
        });

        return root;
    }

    private static Label kpiValue(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("title-1");
        return l;
    }

    private static VBox kpiTile(String caption, Label valueLabel) {
        Label cap = new Label(caption);
        cap.getStyleClass().add("text-muted");
        VBox tile = new VBox(4, valueLabel, cap);
        tile.setPadding(new Insets(16));
        tile.setPrefWidth(240);
        tile.getStyleClass().add("card");
        HBox.setHgrow(tile, Priority.ALWAYS);
        return tile;
    }
}
