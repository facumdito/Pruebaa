package ar.com.rosario.realestate.persistence.adapter;

import ar.com.rosario.realestate.core.domain.Agente;
import ar.com.rosario.realestate.core.domain.AgenteId;
import ar.com.rosario.realestate.core.port.out.AgenteRepository;
import ar.com.rosario.realestate.persistence.entity.JpaAgente;
import ar.com.rosario.realestate.persistence.repository.internal.SpringDataAgenteRepo;
import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaAgenteRepository implements AgenteRepository {

    private final SpringDataAgenteRepo repo;

    public JpaAgenteRepository(SpringDataAgenteRepo repo) {
        this.repo = repo;
    }

    @Override
    public void save(Agente agente) {
        repo.save(toJpa(agente));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Agente> findById(TenantId tenantId, AgenteId id) {
        return repo.findById(id.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agente> findByTenant(TenantId tenantId) {
        return repo.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agente> findActivosByTenant(TenantId tenantId) {
        return repo.findByActivoTrue().stream().map(this::toDomain).toList();
    }

    private JpaAgente toJpa(Agente a) {
        JpaAgente j = new JpaAgente();
        j.setId(a.getId().value());
        j.setTenantId(a.getTenantId().value());
        j.setNombre(a.getNombre());
        j.setEmail(a.getEmail());
        j.setTelefono(a.getTelefono());
        j.setActivo(a.isActivo());
        j.setVersion(a.getVersion());
        j.setSyncState(a.getSyncState());
        j.setCreatedAt(a.getCreatedAt());
        j.setUpdatedAt(a.getUpdatedAt());
        return j;
    }

    private Agente toDomain(JpaAgente j) {
        Agente a = new Agente(AgenteId.of(j.getId()), TenantId.of(j.getTenantId()),
                              j.getNombre(), j.getEmail());
        a.actualizarContacto(j.getEmail(), j.getTelefono());
        if (!j.isActivo()) a.desactivar();
        a.setVersion(j.getVersion() != null ? j.getVersion() : 0L);
        a.setSyncState(j.getSyncState() != null ? j.getSyncState() : SyncState.SYNCED);
        a.setCreatedAt(j.getCreatedAt());
        a.setUpdatedAt(j.getUpdatedAt());
        return a;
    }
}
