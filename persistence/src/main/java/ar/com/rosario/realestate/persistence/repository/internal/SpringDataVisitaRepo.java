package ar.com.rosario.realestate.persistence.repository.internal;

import ar.com.rosario.realestate.core.domain.EstadoVisita;
import ar.com.rosario.realestate.persistence.entity.JpaVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface SpringDataVisitaRepo extends JpaRepository<JpaVisita, String> {

    List<JpaVisita> findByEstado(EstadoVisita estado);

    @Query("SELECT v FROM JpaVisita v WHERE v.estado IN ('AGENDADA','CONFIRMADA') AND v.fechaHora < :before")
    List<JpaVisita> findPendientesBefore(@Param("before") Instant before);
}
