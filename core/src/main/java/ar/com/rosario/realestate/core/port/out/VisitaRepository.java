package ar.com.rosario.realestate.core.port.out;

import ar.com.rosario.realestate.core.domain.EstadoVisita;
import ar.com.rosario.realestate.core.domain.Visita;
import ar.com.rosario.realestate.core.domain.VisitaId;
import ar.com.rosario.realestate.shared.TenantId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VisitaRepository {

    void save(Visita visita);

    Optional<Visita> findById(TenantId tenantId, VisitaId id);

    List<Visita> findByTenantAndEstado(TenantId tenantId, EstadoVisita estado);

    List<Visita> findPendientesBefore(TenantId tenantId, Instant before);
}
