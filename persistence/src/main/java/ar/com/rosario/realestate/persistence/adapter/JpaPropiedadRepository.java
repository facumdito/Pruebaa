package ar.com.rosario.realestate.persistence.adapter;

import ar.com.rosario.realestate.core.domain.EstadoPropiedad;
import ar.com.rosario.realestate.core.domain.Propiedad;
import ar.com.rosario.realestate.core.domain.PropiedadId;
import ar.com.rosario.realestate.core.port.out.PropiedadRepository;
import ar.com.rosario.realestate.persistence.entity.JpaPropiedad;
import ar.com.rosario.realestate.persistence.mapper.PropiedadMapper;
import ar.com.rosario.realestate.persistence.repository.internal.SpringDataPropiedadRepo;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaPropiedadRepository implements PropiedadRepository {

    private final SpringDataPropiedadRepo repo;
    private final PropiedadMapper mapper;

    public JpaPropiedadRepository(SpringDataPropiedadRepo repo, PropiedadMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void save(Propiedad propiedad) {
        repo.save(mapper.toJpa(propiedad));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Propiedad> findById(TenantId tenantId, PropiedadId id) {
        return repo.findByIdAndDeletedFalse(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Propiedad> findByTenant(TenantId tenantId) {
        return repo.findByDeletedFalse().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Propiedad> findByTenantAndEstado(TenantId tenantId, EstadoPropiedad estado) {
        return repo.findByEstadoAndDeletedFalse(estado).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Propiedad> findModifiedSince(TenantId tenantId, Instant since) {
        return repo.findModifiedSince(since).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(TenantId tenantId, PropiedadId id) {
        repo.findByIdAndDeletedFalse(id.value()).ifPresent(jpa -> {
            jpa.setDeleted(true);
            repo.save(jpa);
        });
    }
}
