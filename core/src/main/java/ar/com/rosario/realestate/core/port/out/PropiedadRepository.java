package ar.com.rosario.realestate.core.port.out;

import ar.com.rosario.realestate.core.domain.EstadoPropiedad;
import ar.com.rosario.realestate.core.domain.Propiedad;
import ar.com.rosario.realestate.core.domain.PropiedadId;
import ar.com.rosario.realestate.shared.TenantId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PropiedadRepository {

    void save(Propiedad propiedad);

    Optional<Propiedad> findById(TenantId tenantId, PropiedadId id);

    List<Propiedad> findByTenant(TenantId tenantId);

    List<Propiedad> findByTenantAndEstado(TenantId tenantId, EstadoPropiedad estado);

    List<Propiedad> findModifiedSince(TenantId tenantId, Instant since);

    void delete(TenantId tenantId, PropiedadId id);
}
