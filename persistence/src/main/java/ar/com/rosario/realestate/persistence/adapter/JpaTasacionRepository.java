package ar.com.rosario.realestate.persistence.adapter;

import ar.com.rosario.realestate.core.domain.*;
import ar.com.rosario.realestate.core.port.out.TasacionRepository;
import ar.com.rosario.realestate.persistence.entity.JpaTasacion;
import ar.com.rosario.realestate.persistence.repository.internal.SpringDataTasacionRepo;
import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaTasacionRepository implements TasacionRepository {

    private final SpringDataTasacionRepo repo;

    public JpaTasacionRepository(SpringDataTasacionRepo repo) {
        this.repo = repo;
    }

    @Override
    public void save(Tasacion t) {
        repo.save(toJpa(t));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tasacion> findById(TenantId tenantId, TasacionId id) {
        return repo.findById(id.value()).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tasacion> findByPropiedad(TenantId tenantId, PropiedadId propiedadId) {
        return repo.findByPropiedadIdOrderByFechaDesc(propiedadId.value())
                   .stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tasacion> findUltimaPorPropiedad(TenantId tenantId, PropiedadId propiedadId) {
        return repo.findFirstByPropiedadIdOrderByFechaDesc(propiedadId.value()).map(this::toDomain);
    }

    private JpaTasacion toJpa(Tasacion t) {
        JpaTasacion j = new JpaTasacion();
        j.setId(t.getId().value());
        j.setTenantId(t.getTenantId().value());
        j.setPropiedadId(t.getPropiedadId().value());
        if (t.getTasadorId() != null) j.setTasadorId(t.getTasadorId().value());
        j.setValorEstimadoUsd(t.getValorEstimadoUsd());
        j.setBandaInferiorUsd(t.getBandaInferiorUsd());
        j.setBandaSuperiorUsd(t.getBandaSuperiorUsd());
        j.setMdape(t.getMdape());
        j.setPe10(t.getPe10());
        j.setPe20(t.getPe20());
        j.setMetodologia(t.getMetodologia());
        j.setFecha(t.getFecha());
        j.setModelVersion(t.getModelVersion());
        j.setVersion(t.getVersion());
        j.setSyncState(t.getSyncState());
        j.setCreatedAt(t.getCreatedAt());
        j.setUpdatedAt(t.getUpdatedAt());
        return j;
    }

    private Tasacion toDomain(JpaTasacion j) {
        Tasacion t = new Tasacion(
            TasacionId.of(j.getId()),
            TenantId.of(j.getTenantId()),
            PropiedadId.of(j.getPropiedadId()),
            j.getTasadorId() != null ? AgenteId.of(j.getTasadorId()) : null,
            j.getMetodologia()
        );
        t.setVersion(j.getVersion() != null ? j.getVersion() : 0L);
        t.setSyncState(j.getSyncState() != null ? j.getSyncState() : SyncState.SYNCED);
        t.setCreatedAt(j.getCreatedAt());
        t.setUpdatedAt(j.getUpdatedAt());
        return t;
    }
}
