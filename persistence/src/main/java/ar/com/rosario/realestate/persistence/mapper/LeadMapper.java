package ar.com.rosario.realestate.persistence.mapper;

import ar.com.rosario.realestate.core.domain.Lead;
import ar.com.rosario.realestate.core.domain.LeadId;
import ar.com.rosario.realestate.persistence.entity.JpaLead;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.stereotype.Component;

@Component
public class LeadMapper {

    public JpaLead toJpa(Lead l) {
        JpaLead j = new JpaLead();
        j.setId(l.getId().value());
        j.setTenantId(l.getTenantId().value());
        j.setNombre(l.getNombre());
        j.setFuente(l.getFuente());
        j.setEstado(l.getEstado());
        j.setScore(l.getScore());
        j.setNotas(l.getNotas());
        j.setVersion(l.getVersion());
        j.setSyncState(l.getSyncState());
        j.setDeleted(l.isDeleted());
        j.setCreatedAt(l.getCreatedAt());
        j.setUpdatedAt(l.getUpdatedAt());
        return j;
    }

    public Lead toDomain(JpaLead j) {
        Lead l = new Lead(
            LeadId.of(j.getId()),
            TenantId.of(j.getTenantId()),
            j.getNombre(),
            j.getFuente()
        );
        l.avanzarEstado(j.getEstado());
        l.actualizarScore(j.getScore());
        l.setNotas(j.getNotas());
        l.setVersion(j.getVersion() != null ? j.getVersion() : 0L);
        l.setSyncState(j.getSyncState());
        l.setCreatedAt(j.getCreatedAt());
        l.setUpdatedAt(j.getUpdatedAt());
        return l;
    }
}
