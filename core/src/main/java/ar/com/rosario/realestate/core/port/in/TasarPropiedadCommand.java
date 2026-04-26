package ar.com.rosario.realestate.core.port.in;

import ar.com.rosario.realestate.core.domain.PropiedadId;
import ar.com.rosario.realestate.shared.TenantId;

public record TasarPropiedadCommand(TenantId tenantId, PropiedadId propiedadId) {}
