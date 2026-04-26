package ar.com.rosario.realestate.persistence.repository.internal;

import ar.com.rosario.realestate.core.domain.EstadoPropiedad;
import ar.com.rosario.realestate.persistence.entity.JpaPropiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SpringDataPropiedadRepo extends JpaRepository<JpaPropiedad, String> {

    // @TenantId filter added automatically by Hibernate; no need to specify tenant_id in queries
    List<JpaPropiedad> findByDeletedFalse();

    List<JpaPropiedad> findByEstadoAndDeletedFalse(EstadoPropiedad estado);

    Optional<JpaPropiedad> findByIdAndDeletedFalse(String id);

    @Query("SELECT p FROM JpaPropiedad p WHERE p.updatedAt > :since AND p.deleted = false")
    List<JpaPropiedad> findModifiedSince(@Param("since") Instant since);
}
