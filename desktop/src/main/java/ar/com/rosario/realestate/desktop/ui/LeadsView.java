package ar.com.rosario.realestate.desktop.ui;

import ar.com.rosario.realestate.core.domain.Lead;
import ar.com.rosario.realestate.core.port.out.LeadRepository;
import ar.com.rosario.realestate.desktop.AppContext;
import ar.com.rosario.realestate.shared.TenantContext;
import ar.com.rosario.realestate.shared.TenantId;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class LeadsView {

    private LeadsView() {}

    public static Parent build(String tenantId) {
        Label title = new Label("Leads");
        title.getStyleClass().add("title-2");

        TextField search = new TextField();
        search.setPromptText("Buscar nombre o teléfono…");
        search.setPrefWidth(260);

        TableView<Lead> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        table.getColumns().addAll(List.of(
            col("Nombre",   200, l -> l.getNombre()),
            col("Teléfono", 130, l -> l.getTelefono() != null ? l.getTelefono() : ""),
            col("Email",    200, l -> l.getEmail() != null ? l.getEmail() : ""),
            col("Fuente",   100, l -> l.getFuente() != null ? l.getFuente().name() : ""),
            col("Estado",   130, l -> l.getEstado() != null ? l.getEstado().name() : ""),
            col("Score",     80, l -> String.valueOf(l.getScore()))
        ));

        ObservableList<Lead> data = FXCollections.observableArrayList();
        FilteredList<Lead> filtered = new FilteredList<>(data, x -> true);
        table.setItems(filtered);

        search.textProperty().addListener((obs, o, n) ->
            filtered.setPredicate(l -> n == null || n.isBlank()
                || l.getNombre().toLowerCase().contains(n.toLowerCase())
                || (l.getTelefono() != null && l.getTelefono().contains(n))));

        Label status = new Label("Cargando…");
        status.getStyleClass().add("text-muted");

        VBox root = new VBox(12, title, search, table, status);
        root.setPadding(new Insets(20));

        Thread.ofVirtual().start(() -> {
            try {
                TenantContext.set(tenantId);
                LeadRepository repo = AppContext.get(LeadRepository.class);
                List<Lead> leads = repo.findByTenant(TenantId.of(tenantId));
                Platform.runLater(() -> {
                    data.setAll(leads);
                    status.setText(leads.size() + " leads");
                });
            } catch (Exception e) {
                Platform.runLater(() -> status.setText("Error: " + e.getMessage()));
            } finally {
                TenantContext.clear();
            }
        });

        return root;
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
