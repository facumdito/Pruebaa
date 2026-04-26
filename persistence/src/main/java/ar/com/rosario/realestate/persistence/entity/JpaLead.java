package ar.com.rosario.realestate.persistence.entity;

import ar.com.rosario.realestate.core.domain.EstadoLead;
import ar.com.rosario.realestate.core.domain.FuenteLead;
import ar.com.rosario.realestate.shared.SyncState;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.time.Instant;

@Entity
@Table(name = "leads",
    indexes = {
        @Index(name = "idx_lead_tenant_estado", columnList = "tenant_id,estado")
    }
)
public class JpaLead {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @TenantId
    @Column(name = "tenant_id", length = 36, nullable = false, updatable = false)
    private String tenantId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuente", nullable = false)
    private FuenteLead fuente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoLead estado;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_state", nullable = false)
    private SyncState syncState;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public FuenteLead getFuente() { return fuente; }
    public void setFuente(FuenteLead fuente) { this.fuente = fuente; }
    public EstadoLead getEstado() { return estado; }
    public void setEstado(EstadoLead estado) { this.estado = estado; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public SyncState getSyncState() { return syncState; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
