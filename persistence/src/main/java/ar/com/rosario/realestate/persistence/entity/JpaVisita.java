package ar.com.rosario.realestate.persistence.entity;

import ar.com.rosario.realestate.core.domain.EstadoVisita;
import ar.com.rosario.realestate.shared.SyncState;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.time.Instant;

@Entity
@Table(name = "visitas",
    indexes = {
        @Index(name = "idx_visita_estado",  columnList = "tenant_id,estado"),
        @Index(name = "idx_visita_fecha",   columnList = "tenant_id,fecha_hora"),
        @Index(name = "idx_visita_agente",  columnList = "tenant_id,agente_id")
    }
)
public class JpaVisita {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @TenantId
    @Column(name = "tenant_id", length = 36, nullable = false, updatable = false)
    private String tenantId;

    @Column(name = "propiedad_id", length = 36, nullable = false, updatable = false)
    private String propiedadId;

    @Column(name = "lead_id", length = 36, nullable = false, updatable = false)
    private String leadId;

    @Column(name = "agente_id", length = 36)
    private String agenteId;

    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    @Column(name = "duracion_minutos", nullable = false)
    private int duracionMinutos;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoVisita estado;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_state", nullable = false)
    private SyncState syncState;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getPropiedadId() { return propiedadId; }
    public void setPropiedadId(String propiedadId) { this.propiedadId = propiedadId; }
    public String getLeadId() { return leadId; }
    public void setLeadId(String leadId) { this.leadId = leadId; }
    public String getAgenteId() { return agenteId; }
    public void setAgenteId(String agenteId) { this.agenteId = agenteId; }
    public Instant getFechaHora() { return fechaHora; }
    public void setFechaHora(Instant fechaHora) { this.fechaHora = fechaHora; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public EstadoVisita getEstado() { return estado; }
    public void setEstado(EstadoVisita estado) { this.estado = estado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public SyncState getSyncState() { return syncState; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
