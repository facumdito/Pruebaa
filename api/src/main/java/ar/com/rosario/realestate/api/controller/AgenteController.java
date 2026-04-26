package ar.com.rosario.realestate.api.controller;

import ar.com.rosario.realestate.api.dto.AgenteDto;
import ar.com.rosario.realestate.core.domain.AgenteId;
import ar.com.rosario.realestate.core.port.out.AgenteRepository;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agentes")
public class AgenteController {

    private final AgenteRepository repo;

    public AgenteController(AgenteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<AgenteDto> listar(@AuthenticationPrincipal Jwt jwt,
                                  @RequestParam(defaultValue = "false") boolean soloActivos) {
        TenantId tenantId = tenantFrom(jwt);
        if (soloActivos) {
            return repo.findActivosByTenant(tenantId).stream().map(AgenteDto::from).toList();
        }
        return repo.findByTenant(tenantId).stream().map(AgenteDto::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgenteDto> obtener(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        return repo.findById(tenantFrom(jwt), AgenteId.of(id))
            .map(AgenteDto::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    private TenantId tenantFrom(Jwt jwt) {
        return TenantId.of(jwt.getClaimAsString("tenant_id"));
    }
}
