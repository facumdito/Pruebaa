package ar.com.rosario.realestate.persistence.repository.internal;

import ar.com.rosario.realestate.core.domain.EstadoLead;
import ar.com.rosario.realestate.persistence.entity.JpaLead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataLeadRepo extends JpaRepository<JpaLead, String> {

    List<JpaLead> findByDeletedFalse();

    List<JpaLead> findByEstadoAndDeletedFalse(EstadoLead estado);

    Optional<JpaLead> findByIdAndDeletedFalse(String id);
}
