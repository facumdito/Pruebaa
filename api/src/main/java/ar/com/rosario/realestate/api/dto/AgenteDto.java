package ar.com.rosario.realestate.api.dto;

import ar.com.rosario.realestate.core.domain.Agente;

import java.time.Instant;

public record AgenteDto(
    String id,
    String tenantId,
    String nombre,
    String email,
    String telefono,
    boolean activo,
    long version,
    Instant createdAt,
    Instant updatedAt
) {
    public static AgenteDto from(Agente a) {
        return new AgenteDto(
            a.getId().value(),
            a.getTenantId().value(),
            a.getNombre(),
            a.getEmail(),
            a.getTelefono(),
            a.isActivo(),
            a.getVersion(),
            a.getCreatedAt(),
            a.getUpdatedAt()
        );
    }
}
