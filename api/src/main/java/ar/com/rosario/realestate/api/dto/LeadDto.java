package ar.com.rosario.realestate.api.dto;

import ar.com.rosario.realestate.core.domain.*;

import java.time.Instant;

public record LeadDto(
    String id,
    String tenantId,
    String nombre,
    String telefono,
    String email,
    FuenteLead fuente,
    EstadoLead estado,
    int bantBudget,
    int bantAuthority,
    int bantNeed,
    int bantTimeline,
    int score,
    String notas,
    long version,
    Instant createdAt,
    Instant updatedAt
) {
    public static LeadDto from(Lead l) {
        return new LeadDto(
            l.getId().value(),
            l.getTenantId().value(),
            l.getNombre(),
            l.getTelefono(),
            l.getEmail(),
            l.getFuente(),
            l.getEstado(),
            l.getBantBudget(),
            l.getBantAuthority(),
            l.getBantNeed(),
            l.getBantTimeline(),
            l.getScore(),
            l.getNotas(),
            l.getVersion(),
            l.getCreatedAt(),
            l.getUpdatedAt()
        );
    }
}
