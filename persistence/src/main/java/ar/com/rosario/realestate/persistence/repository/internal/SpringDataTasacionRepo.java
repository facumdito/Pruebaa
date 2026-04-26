package ar.com.rosario.realestate.persistence.repository.internal;

import ar.com.rosario.realestate.persistence.entity.JpaTasacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataTasacionRepo extends JpaRepository<JpaTasacion, String> {

    List<JpaTasacion> findByPropiedadIdOrderByFechaDesc(String propiedadId);

    Optional<JpaTasacion> findFirstByPropiedadIdOrderByFechaDesc(String propiedadId);
}
