package ar.com.rosario.realestate.api.controller;

import ar.com.rosario.realestate.api.dto.VisitaDto;
import ar.com.rosario.realestate.api.service.AgendarVisitaService;
import ar.com.rosario.realestate.core.domain.AgenteId;
import ar.com.rosario.realestate.core.domain.LeadId;
import ar.com.rosario.realestate.core.domain.PropiedadId;
import ar.com.rosario.realestate.core.domain.VisitaId;
import ar.com.rosario.realestate.core.port.in.AgendarVisitaCommand;
import ar.com.rosario.realestate.core.port.out.VisitaRepository;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/visitas")
public class VisitaController {

    private final VisitaRepository repo;
    private final AgendarVisitaService agendarService;

    public VisitaController(VisitaRepository repo, AgendarVisitaService agendarService) {
        this.repo = repo;
        this.agendarService = agendarService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitaDto> obtener(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        return repo.findById(tenantFrom(jwt), VisitaId.of(id))
            .map(VisitaDto::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitaDto agendar(@AuthenticationPrincipal Jwt jwt,
                             @RequestBody AgendarVisitaRequest body) {
        TenantId tenantId = tenantFrom(jwt);
        var visita = agendarService.agendar(new AgendarVisitaCommand(
            tenantId,
            PropiedadId.of(body.propiedadId()),
            LeadId.of(body.leadId()),
            body.agenteId() != null ? AgenteId.of(body.agenteId()) : null,
            body.fechaHora(),
            body.duracionMinutos()
        ));
        return VisitaDto.from(visita);
    }

    private TenantId tenantFrom(Jwt jwt) {
        return TenantId.of(jwt.getClaimAsString("tenant_id"));
    }

    public record AgendarVisitaRequest(
        String propiedadId,
        String leadId,
        String agenteId,
        Instant fechaHora,
        int duracionMinutos
    ) {}
}
