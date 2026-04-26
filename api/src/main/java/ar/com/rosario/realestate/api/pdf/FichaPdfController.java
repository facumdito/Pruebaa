package ar.com.rosario.realestate.api.pdf;

import ar.com.rosario.realestate.core.domain.PropiedadId;
import ar.com.rosario.realestate.core.port.out.PropiedadRepository;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/propiedades/{id}/ficha")
public class FichaPdfController {

    private final PropiedadRepository repo;
    private final FichaPdfService pdfService;

    public FichaPdfController(PropiedadRepository repo, FichaPdfService pdfService) {
        this.repo = repo;
        this.pdfService = pdfService;
    }

    @GetMapping(produces = "application/pdf")
    public ResponseEntity<byte[]> descargar(@AuthenticationPrincipal Jwt jwt,
                                             @PathVariable String id) {
        TenantId tenantId = TenantId.of(jwt.getClaimAsString("tenant_id"));
        var propiedad = repo.findById(tenantId, PropiedadId.of(id))
            .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada: " + id));

        byte[] pdf = pdfService.generarFichaPropiedad(propiedad);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ficha-" + id + ".pdf\"")
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(pdf.length)
            .body(pdf);
    }
}
