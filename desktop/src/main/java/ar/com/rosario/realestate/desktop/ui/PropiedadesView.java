package ar.com.rosario.realestate.desktop.ui;

import ar.com.rosario.realestate.core.domain.EstadoPropiedad;
import ar.com.rosario.realestate.core.domain.Propiedad;
import ar.com.rosario.realestate.core.port.out.PropiedadRepository;
import ar.com.rosario.realestate.desktop.AppContext;
import ar.com.rosario.realestate.shared.TenantContext;
import ar.com.rosario.realestate.shared.TenantId;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class PropiedadesView {

    private PropiedadesView() {}

    public static Parent build(String tenantId) {
        Label title = new Label("Propiedades");
        title.getStyleClass().add("title-2");

        // Filter bar
        TextField search = new TextField();
        search.setPromptText("Buscar dirección…");
        search.setPrefWidth(260);

        ComboBox<String> estadoFilter = new ComboBox<>();
        estadoFilter.getItems().add("Todos");
        for (EstadoPropiedad e : EstadoPropiedad.values()) estadoFilter.getItems().add(e.name());
        estadoFilter.setValue("Todos");

        HBox filters = new HBox(8, search, estadoFilter);

        // Table
        TableView<Propiedad> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        table.getColumns().addAll(List.of(
            col("Dirección",   550, p -> p.getDireccion()),
            col("Barrio",      160, p -> p.getBarrio() != null ? p.getBarrio().nombre() : ""),
            col("Tipo",        110, p -> p.getTipo() != null ? p.getTipo().name() : ""),
            col("Operación",   100, p -> p.getOperacion() != null ? p.getOperacion().name() : ""),
            col("Estado",      110, p -> p.getEstado() != null ? p.getEstado().name() : ""),
            col("Precio USD",  130, p -> p.getPrecioUsd() != null ? "USD " + p.getPrecioUsd().toPlainString() : "—")
        ));

        ObservableList<Propiedad> data = FXCollections.observableArrayList();
        FilteredList<Propiedad> filtered = new FilteredList<>(data, x -> true);
        table.setItems(filtered);

        // Search + filter listeners
        search.textProperty().addListener((obs, o, n) -> applyFilter(filtered, n, estadoFilter.getValue()));
        estadoFilter.valueProperty().addListener((obs, o, n) -> applyFilter(filtered, search.getText(), n));

        Label status = new Label("Cargando…");
        status.getStyleClass().add("text-muted");

        VBox root = new VBox(12, title, filters, table, status);
        root.setPadding(new Insets(20));

        // Async load
        Thread.ofVirtual().start(() -> {
            try {
                TenantContext.set(tenantId);
                PropiedadRepository repo = AppContext.get(PropiedadRepository.class);
                List<Propiedad> propiedades = repo.findByTenant(TenantId.of(tenantId));
                Platform.runLater(() -> {
                    data.setAll(propiedades);
                    status.setText(propiedades.size() + " propiedades");
                });
            } catch (Exception e) {
                Platform.runLater(() -> status.setText("Error: " + e.getMessage()));
            } finally {
                TenantContext.clear();
            }
        });

        return root;
    }

    private static void applyFilter(FilteredList<Propiedad> list, String text, String estado) {
        list.setPredicate(p -> {
            boolean matchText = text == null || text.isBlank()
                || p.getDireccion().toLowerCase().contains(text.toLowerCase());
            boolean matchEstado = "Todos".equals(estado)
                || (p.getEstado() != null && p.getEstado().name().equals(estado));
            return matchText && matchEstado;
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> TableColumn<T, String> col(String title, double width,
                                                    java.util.function.Function<T, String> fn) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setCellValueFactory(cell ->
            new javafx.beans.property.SimpleStringProperty(fn.apply(cell.getValue())));
        return col;
    }
}
