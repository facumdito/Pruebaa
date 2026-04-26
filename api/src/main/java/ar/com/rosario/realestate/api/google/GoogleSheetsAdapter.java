package ar.com.rosario.realestate.api.google;

import ar.com.rosario.realestate.core.domain.Propiedad;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Syncs Propiedad listings to a Google Sheet for broker reporting.
 * Sheet columns: id | direccion | barrio | tipo | operacion | estado | precioUsd | m2Cub | m2Tot | updatedAt
 */
@Component
public class GoogleSheetsAdapter {

    private static final Logger log = LoggerFactory.getLogger(GoogleSheetsAdapter.class);
    private static final List<String> SCOPES = List.of(SheetsScopes.SPREADSHEETS);
    private static final String APP_NAME = "RosarioRealEstate";
    private static final String RANGE = "Propiedades!A1";

    private final GoogleCredentialProvider credProvider;
    private final String spreadsheetId;

    public GoogleSheetsAdapter(GoogleCredentialProvider credProvider,
                                @Value("${google.sheets.spreadsheet-id:}") String spreadsheetId) {
        this.credProvider = credProvider;
        this.spreadsheetId = spreadsheetId;
    }

    public void syncPropiedades(List<Propiedad> propiedades) {
        if (spreadsheetId.isBlank()) {
            log.info("[Sheets STUB] syncPropiedades size={}", propiedades.size());
            return;
        }
        try {
            Sheets service = buildService();
            List<List<Object>> rows = buildRows(propiedades);

            // Clear existing data then write fresh
            service.spreadsheets().values()
                    .clear(spreadsheetId, "Propiedades!A2:Z", new ClearValuesRequest())
                    .execute();

            ValueRange body = new ValueRange().setValues(rows);
            service.spreadsheets().values()
                    .update(spreadsheetId, RANGE, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();

            log.info("Synced {} propiedades to Google Sheets", propiedades.size());
        } catch (IOException e) {
            throw new RuntimeException("Error syncing to Google Sheets: " + e.getMessage(), e);
        }
    }

    public void ensureHeader() {
        if (spreadsheetId.isBlank()) return;
        try {
            Sheets service = buildService();
            List<Object> header = List.of(
                "ID", "Dirección", "Barrio", "Tipo", "Operación",
                "Estado", "Precio USD", "m² Cubiertos", "m² Totales",
                "Fecha Cotización", "Actualizado"
            );
            ValueRange body = new ValueRange().setValues(List.of(header));
            service.spreadsheets().values()
                    .update(spreadsheetId, "Propiedades!A1", body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
        } catch (IOException e) {
            log.warn("Could not write sheet header: {}", e.getMessage());
        }
    }

    private List<List<Object>> buildRows(List<Propiedad> propiedades) {
        return propiedades.stream().map(p -> List.<Object>of(
            p.getId().value(),
            p.getDireccion(),
            p.getBarrio() != null ? p.getBarrio().nombre() : "",
            p.getTipo() != null ? p.getTipo().name() : "",
            p.getOperacion() != null ? p.getOperacion().name() : "",
            p.getEstado() != null ? p.getEstado().name() : "",
            p.getPrecioUsd() != null ? p.getPrecioUsd().toPlainString() : "",
            p.getM2Cubiertos() != null ? p.getM2Cubiertos().toPlainString() : "",
            p.getM2Totales() != null ? p.getM2Totales().toPlainString() : "",
            p.getFechaCotizacion() != null ? p.getFechaCotizacion().toString() : "",
            p.getUpdatedAt() != null ? p.getUpdatedAt().toString() : ""
        )).toList();
    }

    private Sheets buildService() throws IOException {
        return new Sheets.Builder(
                credProvider.transport(),
                GoogleCredentialProvider.JSON_FACTORY,
                credProvider.credentials(SCOPES))
                .setApplicationName(APP_NAME)
                .build();
    }
}
