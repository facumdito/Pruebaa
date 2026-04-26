package ar.com.rosario.realestate.persistence.entity;

import ar.com.rosario.realestate.core.domain.MetodologiaTasacion;
import ar.com.rosario.realestate.shared.SyncState;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tasaciones",
    indexes = {
        @Index(name = "idx_tasacion_propiedad", columnList = "tenant_id,propiedad_id,fecha")
    }
)
public class JpaTasacion {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @TenantId
    @Column(name = "tenant_id", length = 36, nullable = false, updatable = false)
    private String tenantId;

    @Column(name = "propiedad_id", length = 36, nullable = false, updatable = false)
    private String propiedadId;

    @Column(name = "tasador_id", length = 36)
    private String tasadorId;

    @Column(name = "valor_estimado_usd", precision = 15, scale = 2)
    private BigDecimal valorEstimadoUsd;

    @Column(name = "banda_inferior_usd", precision = 15, scale = 2)
    private BigDecimal bandaInferiorUsd;

    @Column(name = "banda_superior_usd", precision = 15, scale = 2)
    private BigDecimal bandaSuperiorUsd;

    @Column(name = "mdape", precision = 6, scale = 4)
    private Double mdape;

    @Column(name = "pe10", precision = 6, scale = 4)
    private Double pe10;

    @Column(name = "pe20", precision = 6, scale = 4)
    private Double pe20;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodologia", nullable = false)
    private MetodologiaTasacion metodologia;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "model_version", length = 64)
    private String modelVersion;

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
    public String getTasadorId() { return tasadorId; }
    public void setTasadorId(String tasadorId) { this.tasadorId = tasadorId; }
    public BigDecimal getValorEstimadoUsd() { return valorEstimadoUsd; }
    public void setValorEstimadoUsd(BigDecimal v) { this.valorEstimadoUsd = v; }
    public BigDecimal getBandaInferiorUsd() { return bandaInferiorUsd; }
    public void setBandaInferiorUsd(BigDecimal v) { this.bandaInferiorUsd = v; }
    public BigDecimal getBandaSuperiorUsd() { return bandaSuperiorUsd; }
    public void setBandaSuperiorUsd(BigDecimal v) { this.bandaSuperiorUsd = v; }
    public Double getMdape() { return mdape; }
    public void setMdape(Double mdape) { this.mdape = mdape; }
    public Double getPe10() { return pe10; }
    public void setPe10(Double pe10) { this.pe10 = pe10; }
    public Double getPe20() { return pe20; }
    public void setPe20(Double pe20) { this.pe20 = pe20; }
    public MetodologiaTasacion getMetodologia() { return metodologia; }
    public void setMetodologia(MetodologiaTasacion m) { this.metodologia = m; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public SyncState getSyncState() { return syncState; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
