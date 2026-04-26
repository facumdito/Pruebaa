package ar.com.rosario.realestate.core.port.in;

import ar.com.rosario.realestate.core.domain.AgenteId;
import ar.com.rosario.realestate.core.domain.LeadId;
import ar.com.rosario.realestate.core.domain.PropiedadId;
import ar.com.rosario.realestate.shared.TenantId;

import java.time.Instant;

public record AgendarVisitaCommand(
    TenantId tenantId,
    PropiedadId propiedadId,
    LeadId leadId,
    AgenteId agenteId,
    Instant fechaHora,
    int duracionMinutos
) {}
