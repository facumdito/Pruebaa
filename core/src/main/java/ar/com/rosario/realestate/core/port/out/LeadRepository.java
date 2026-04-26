package ar.com.rosario.realestate.core.port.out;

import ar.com.rosario.realestate.core.domain.EstadoLead;
import ar.com.rosario.realestate.core.domain.Lead;
import ar.com.rosario.realestate.core.domain.LeadId;
import ar.com.rosario.realestate.shared.TenantId;

import java.util.List;
import java.util.Optional;

public interface LeadRepository {

    void save(Lead lead);

    Optional<Lead> findById(TenantId tenantId, LeadId id);

    List<Lead> findByTenantAndEstado(TenantId tenantId, EstadoLead estado);

    List<Lead> findByTenant(TenantId tenantId);
}
