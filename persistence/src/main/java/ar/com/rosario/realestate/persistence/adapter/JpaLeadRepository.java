package ar.com.rosario.realestate.persistence.adapter;

import ar.com.rosario.realestate.core.domain.EstadoLead;
import ar.com.rosario.realestate.core.domain.Lead;
import ar.com.rosario.realestate.core.domain.LeadId;
import ar.com.rosario.realestate.core.port.out.LeadRepository;
import ar.com.rosario.realestate.persistence.mapper.LeadMapper;
import ar.com.rosario.realestate.persistence.repository.internal.SpringDataLeadRepo;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaLeadRepository implements LeadRepository {

    private final SpringDataLeadRepo repo;
    private final LeadMapper mapper;

    public JpaLeadRepository(SpringDataLeadRepo repo, LeadMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void save(Lead lead) {
        repo.save(mapper.toJpa(lead));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lead> findById(TenantId tenantId, LeadId id) {
        return repo.findByIdAndDeletedFalse(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lead> findByTenantAndEstado(TenantId tenantId, EstadoLead estado) {
        return repo.findByEstadoAndDeletedFalse(estado).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lead> findByTenant(TenantId tenantId) {
        return repo.findByDeletedFalse().stream().map(mapper::toDomain).toList();
    }
}
