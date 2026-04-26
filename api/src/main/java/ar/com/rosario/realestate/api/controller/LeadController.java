package ar.com.rosario.realestate.api.controller;

import ar.com.rosario.realestate.api.dto.LeadDto;
import ar.com.rosario.realestate.api.service.RegistrarLeadService;
import ar.com.rosario.realestate.core.domain.EstadoLead;
import ar.com.rosario.realestate.core.domain.FuenteLead;
import ar.com.rosario.realestate.core.domain.LeadId;
import ar.com.rosario.realestate.core.port.in.RegistrarLeadCommand;
import ar.com.rosario.realestate.core.port.out.LeadRepository;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadController {

    private final LeadRepository repo;
    private final RegistrarLeadService registrarService;

    public LeadController(LeadRepository repo, RegistrarLeadService registrarService) {
        this.repo = repo;
        this.registrarService = registrarService;
    }

    @GetMapping
    public List<LeadDto> listar(@AuthenticationPrincipal Jwt jwt,
                                @RequestParam(required = false) EstadoLead estado) {
        TenantId tenantId = tenantFrom(jwt);
        if (estado != null) {
            return repo.findByTenantAndEstado(tenantId, estado).stream().map(LeadDto::from).toList();
        }
        return repo.findByTenant(tenantId).stream().map(LeadDto::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadDto> obtener(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        return repo.findById(tenantFrom(jwt), LeadId.of(id))
            .map(LeadDto::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeadDto registrar(@AuthenticationPrincipal Jwt jwt,
                             @RequestBody RegistrarLeadRequest body) {
        TenantId tenantId = tenantFrom(jwt);
        var lead = registrarService.registrar(new RegistrarLeadCommand(
            tenantId, body.nombre(), body.telefono(), body.email(), body.fuente()
        ));
        return LeadDto.from(lead);
    }

    private TenantId tenantFrom(Jwt jwt) {
        return TenantId.of(jwt.getClaimAsString("tenant_id"));
    }

    public record RegistrarLeadRequest(String nombre, String telefono, String email, FuenteLead fuente) {}
}
