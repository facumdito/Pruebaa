package ar.com.rosario.realestate.persistence.adapter;

import ar.com.rosario.realestate.core.domain.*;
import ar.com.rosario.realestate.core.port.out.VisitaRepository;
import ar.com.rosario.realestate.persistence.entity.JpaVisita;
import ar.com.rosario.realestate.persistence.repository.internal.SpringDataVisitaRepo;
import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaVisitaRepository implements VisitaRepository {

    private final SpringDataVisitaRepo repo;

    public JpaVisitaRepository(SpringDataVisitaRepo repo) {
        this.repo = repo;
    }

    @Override
    public void save(Visita visita) {
        repo.save(toJpa(visita));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Visita> findById(TenantId tenantId, VisitaId id) {
        return repo.findById(id.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Visita> findByTenantAndEstado(TenantId tenantId, EstadoVisita estado) {
        return repo.findByEstado(estado).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Visita> findPendientesBefore(TenantId tenantId, Instant before) {
        return repo.findPendientesBefore(before).stream().map(this::toDomain).toList();
    }

    private JpaVisita toJpa(Visita v) {
        JpaVisita j = new JpaVisita();
        j.setId(v.getId().value());
        j.setTenantId(v.getTenantId().value());
        j.setPropiedadId(v.getPropiedadId().value());
        j.setLeadId(v.getLeadId().value());
        j.setAgenteId(v.getAgenteId() != null ? v.getAgenteId().value() : null);
        j.setFechaHora(v.getFechaHora());
        j.setDuracionMinutos(v.getDuracionMinutos());
        j.setEstado(v.getEstado());
        j.setNotas(v.getNotas());
        j.setVersion(v.getVersion());
        j.setSyncState(v.getSyncState());
        j.setCreatedAt(v.getCreatedAt());
        j.setUpdatedAt(v.getUpdatedAt());
        return j;
    }

    private Visita toDomain(JpaVisita j) {
        Visita v = new Visita(
            VisitaId.of(j.getId()),
            TenantId.of(j.getTenantId()),
            PropiedadId.of(j.getPropiedadId()),
            LeadId.of(j.getLeadId()),
            j.getAgenteId() != null ? AgenteId.of(j.getAgenteId()) : null,
            j.getFechaHora(),
            j.getDuracionMinutos()
        );
        if (j.getEstado() != EstadoVisita.AGENDADA) {
            switch (j.getEstado()) {
                case CONFIRMADA -> v.confirmar();
                case REALIZADA  -> v.realizar(j.getNotas());
                case CANCELADA  -> v.cancelar(j.getNotas());
                case NO_SHOW    -> { v.confirmar(); v.marcarNoShow(); }
                default         -> {}
            }
        }
        v.setVersion(j.getVersion() != null ? j.getVersion() : 0L);
        v.setSyncState(j.getSyncState() != null ? j.getSyncState() : SyncState.SYNCED);
        v.setCreatedAt(j.getCreatedAt());
        v.setUpdatedAt(j.getUpdatedAt());
        return v;
    }
}
