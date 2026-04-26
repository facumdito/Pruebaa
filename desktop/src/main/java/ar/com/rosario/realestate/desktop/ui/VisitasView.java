package ar.com.rosario.realestate.desktop.ui;

import ar.com.rosario.realestate.core.domain.EstadoVisita;
import ar.com.rosario.realestate.core.domain.Visita;
import ar.com.rosario.realestate.core.port.out.VisitaRepository;
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

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VisitasView {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.of("America/Argentina/Buenos_Aires"));

    private VisitasView() {}

    public static Parent build(String tenantId) {
        Label title = new Label("Visitas");
        title.getStyleClass().add("title-2");

        ComboBox<EstadoVisita> estadoBox = new ComboBox<>();
        estadoBox.getItems().addAll(EstadoVisita.values());
        estadoBox.setPromptText("Filtrar por estado…");

        Button clearBtn = new Button("Todos");
        clearBtn.setOnAction(e -> estadoBox.setValue(null));

        HBox filters = new HBox(8, estadoBox, clearBtn);

        TableView<Visita> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        table.getColumns().addAll(List.of(
            col("Fecha/Hora", 150, v -> FMT.format(v.getFechaHora())),
            col("Propiedad",  260, v -> v.getPropiedadId().value()),
            col("Lead",       260, v -> v.getLeadId().value()),
            col("Duración",    90, v -> v.getDuracionMinutos() + " min"),
            col("Estado",     110, v -> v.getEstado().name()),
            col("Notas",      200, v -> v.getNotas() != null ? v.getNotas() : "")
        ));

        ObservableList<Visita> data = FXCollections.observableArrayList();
        table.setItems(data);

        estadoBox.valueProperty().addListener((obs, o, estado) -> loadData(tenantId, estado, data, null));

        Label status = new Label("Cargando…");
        status.getStyleClass().add("text-muted");

        VBox root = new VBox(12, title, filters, table, status);
        root.setPadding(new Insets(20));

        loadData(tenantId, null, data, status);
        return root;
    }

    private static void loadData(String tenantId, EstadoVisita estado,
                                   ObservableList<Visita> data, Label status) {
        Thread.ofVirtual().start(() -> {
            try {
                TenantContext.set(tenantId);
                VisitaRepository repo = AppContext.get(VisitaRepository.class);
                List<Visita> visitas = estado != null
                    ? repo.findByTenantAndEstado(TenantId.of(tenantId), estado)
                    : repo.findByTenantAndEstado(TenantId.of(tenantId), EstadoVisita.AGENDADA);
                Platform.runLater(() -> {
                    data.setAll(visitas);
                    if (status != null) status.setText(visitas.size() + " visitas");
                });
            } catch (Exception e) {
                if (status != null) Platform.runLater(() -> status.setText("Error: " + e.getMessage()));
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
