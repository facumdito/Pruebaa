package ar.com.rosario.realestate.desktop.ui;

import ar.com.rosario.realestate.core.domain.Agente;
import ar.com.rosario.realestate.core.port.out.AgenteRepository;
import ar.com.rosario.realestate.desktop.AppContext;
import ar.com.rosario.realestate.shared.TenantContext;
import ar.com.rosario.realestate.shared.TenantId;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class AgentesView {

    private AgentesView() {}

    public static Parent build(String tenantId) {
        Label title = new Label("Agentes");
        title.getStyleClass().add("title-2");

        CheckBox soloActivos = new CheckBox("Solo activos");
        soloActivos.setSelected(true);

        TableView<Agente> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        table.getColumns().addAll(List.of(
            col("Nombre",   220, a -> a.getNombre()),
            col("Email",    240, a -> a.getEmail() != null ? a.getEmail() : ""),
            col("Teléfono", 140, a -> a.getTelefono() != null ? a.getTelefono() : ""),
            col("Estado",   100, a -> a.isActivo() ? "Activo" : "Inactivo")
        ));

        ObservableList<Agente> data = FXCollections.observableArrayList();
        table.setItems(data);

        Label status = new Label("Cargando…");
        status.getStyleClass().add("text-muted");

        soloActivos.selectedProperty().addListener((obs, o, activo) ->
            loadData(tenantId, activo, data, status));

        VBox root = new VBox(12, title, soloActivos, table, status);
        root.setPadding(new Insets(20));

        loadData(tenantId, true, data, status);
        return root;
    }

    private static void loadData(String tenantId, boolean soloActivos,
                                   ObservableList<Agente> data, Label status) {
        Thread.ofVirtual().start(() -> {
            try {
                TenantContext.set(tenantId);
                AgenteRepository repo = AppContext.get(AgenteRepository.class);
                List<Agente> agentes = soloActivos
                    ? repo.findActivosByTenant(TenantId.of(tenantId))
                    : repo.findByTenant(TenantId.of(tenantId));
                Platform.runLater(() -> {
                    data.setAll(agentes);
                    status.setText(agentes.size() + " agentes");
                });
            } catch (Exception e) {
                Platform.runLater(() -> status.setText("Error: " + e.getMessage()));
            } finally {
                TenantContext.clear();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> TableColumn<T, String> col(String title, double width,
                                                    java.util.function.Function<T, String> fn) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(c -> new SimpleStringProperty(fn.apply(c.getValue())));
        return col;
    }
}
