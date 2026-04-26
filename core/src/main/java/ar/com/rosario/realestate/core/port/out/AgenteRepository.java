package ar.com.rosario.realestate.core.port.out;

import ar.com.rosario.realestate.core.domain.Agente;
import ar.com.rosario.realestate.core.domain.AgenteId;
import ar.com.rosario.realestate.shared.TenantId;

import java.util.List;
import java.util.Optional;

public interface AgenteRepository {

    void save(Agente agente);

    Optional<Agente> findById(TenantId tenantId, AgenteId id);

    List<Agente> findByTenant(TenantId tenantId);

    List<Agente> findActivosByTenant(TenantId tenantId);
}
