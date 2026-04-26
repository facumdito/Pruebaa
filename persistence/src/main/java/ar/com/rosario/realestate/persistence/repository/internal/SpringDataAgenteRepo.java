package ar.com.rosario.realestate.persistence.repository.internal;

import ar.com.rosario.realestate.persistence.entity.JpaAgente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataAgenteRepo extends JpaRepository<JpaAgente, String> {

    List<JpaAgente> findAll();

    List<JpaAgente> findByActivoTrue();
}
