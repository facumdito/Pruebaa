package ar.com.rosario.realestate.api.dto;

import ar.com.rosario.realestate.core.domain.*;

import java.time.Instant;

public record VisitaDto(
    String id,
    String tenantId,
    String propiedadId,
    String leadId,
    String agenteId,
    Instant fechaHora,
    int duracionMinutos,
    EstadoVisita estado,
    String notas,
    long version,
    Instant createdAt,
    Instant updatedAt
) {
    public static VisitaDto from(Visita v) {
        return new VisitaDto(
            v.getId().value(),
            v.getTenantId().value(),
            v.getPropiedadId().value(),
            v.getLeadId().value(),
            v.getAgenteId() != null ? v.getAgenteId().value() : null,
            v.getFechaHora(),
            v.getDuracionMinutos(),
            v.getEstado(),
            v.getNotas(),
            v.getVersion(),
            v.getCreatedAt(),
            v.getUpdatedAt()
        );
    }
}
