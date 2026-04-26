package ar.com.rosario.realestate.core.port.in;

import ar.com.rosario.realestate.core.domain.FuenteLead;
import ar.com.rosario.realestate.shared.TenantId;

public record RegistrarLeadCommand(
    TenantId tenantId,
    String nombre,
    String telefono,
    String email,
    FuenteLead fuente
) {}
