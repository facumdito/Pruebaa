package ar.com.rosario.realestate.api.controller;

import ar.com.rosario.realestate.api.dto.PropiedadDto;
import ar.com.rosario.realestate.api.dto.TasacionDto;
import ar.com.rosario.realestate.api.service.TasarPropiedadService;
import ar.com.rosario.realestate.core.domain.EstadoPropiedad;
import ar.com.rosario.realestate.core.domain.PropiedadId;
import ar.com.rosario.realestate.core.port.in.TasarPropiedadCommand;
import ar.com.rosario.realestate.core.port.out.PropiedadRepository;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/propiedades")
public class PropiedadController {

    private final PropiedadRepository repo;
    private final TasarPropiedadService tasarService;

    public PropiedadController(PropiedadRepository repo, TasarPropiedadService tasarService) {
        this.repo = repo;
        this.tasarService = tasarService;
    }

    @GetMapping
    public List<PropiedadDto> listar(@AuthenticationPrincipal Jwt jwt,
                                     @RequestParam(required = false) EstadoPropiedad estado) {
        TenantId tenantId = tenantFrom(jwt);
        if (estado != null) {
            return repo.findByTenantAndEstado(tenantId, estado).stream().map(PropiedadDto::from).toList();
        }
        return repo.findByTenant(tenantId).stream().map(PropiedadDto::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadDto> obtener(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable String id) {
        return repo.findById(tenantFrom(jwt), PropiedadId.of(id))
            .map(PropiedadDto::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/tasar")
    public TasacionDto tasar(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
        TenantId tenantId = tenantFrom(jwt);
        PropiedadId propiedadId = PropiedadId.of(id);
        var result = tasarService.tasar(new TasarPropiedadCommand(tenantId, propiedadId));
        return TasacionDto.from(id, result);
    }

    private TenantId tenantFrom(Jwt jwt) {
        return TenantId.of(jwt.getClaimAsString("tenant_id"));
    }
}
