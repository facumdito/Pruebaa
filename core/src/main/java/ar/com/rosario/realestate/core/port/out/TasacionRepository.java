package ar.com.rosario.realestate.core.port.out;

import ar.com.rosario.realestate.core.domain.Tasacion;
import ar.com.rosario.realestate.core.domain.TasacionId;
import ar.com.rosario.realestate.core.domain.PropiedadId;
import ar.com.rosario.realestate.shared.TenantId;

import java.util.List;
import java.util.Optional;

public interface TasacionRepository {

    void save(Tasacion tasacion);

    Optional<Tasacion> findById(TenantId tenantId, TasacionId id);

    List<Tasacion> findByPropiedad(TenantId tenantId, PropiedadId propiedadId);

    Optional<Tasacion> findUltimaPorPropiedad(TenantId tenantId, PropiedadId propiedadId);
}
