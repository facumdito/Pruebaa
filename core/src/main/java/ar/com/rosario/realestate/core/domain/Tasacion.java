package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Persisted appraisal record — wraps an AVM result or manual valuation.
 * Distinct from {@link TasacionResult} which is the transient AVM output.
 */
public class Tasacion {

    private final TasacionId id;
    private final TenantId tenantId;
    private final PropiedadId propiedadId;

    private AgenteId tasadorId;
    private BigDecimal valorEstimadoUsd;
    private BigDecimal bandaInferiorUsd;
    private BigDecimal bandaSuperiorUsd;
    private double mdape;
    private double pe10;
    private double pe20;
    private MetodologiaTasacion metodologia;
    private LocalDate fecha;
    private String modelVersion;

    private long version;
    private SyncState syncState;
    private Instant createdAt;
    private Instant updatedAt;

    public Tasacion(TasacionId id, TenantId tenantId, PropiedadId propiedadId,
                    AgenteId tasadorId, MetodologiaTasacion metodologia) {
        this.id = id;
        this.tenantId = tenantId;
        this.propiedadId = propiedadId;
        this.tasadorId = tasadorId;
        this.metodologia = metodologia;
        this.fecha = LocalDate.now();
        this.version = 0;
        this.syncState = SyncState.LOCAL;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static Tasacion desdeResultadoAvm(TasacionId id, TenantId tenantId,
                                              AgenteId tasadorId, TasacionResult resultado) {
        Tasacion t = new Tasacion(id, tenantId, resultado.propiedadId(),
                                   tasadorId, MetodologiaTasacion.AVM_PMML);
        t.valorEstimadoUsd = resultado.valorEstimadoUsd();
        t.bandaInferiorUsd = resultado.bandaInferiorUsd();
        t.bandaSuperiorUsd = resultado.bandaSuperiorUsd();
        t.mdape = resultado.mdape();
        t.pe10 = resultado.pe10();
        t.pe20 = resultado.pe20();
        t.modelVersion = resultado.modelVersion();
        return t;
    }

    public boolean isHighConfidence() {
        return pe10 >= 0.70;
    }

    public TasacionId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public PropiedadId getPropiedadId() { return propiedadId; }
    public AgenteId getTasadorId() { return tasadorId; }
    public BigDecimal getValorEstimadoUsd() { return valorEstimadoUsd; }
    public BigDecimal getBandaInferiorUsd() { return bandaInferiorUsd; }
    public BigDecimal getBandaSuperiorUsd() { return bandaSuperiorUsd; }
    public double getMdape() { return mdape; }
    public double getPe10() { return pe10; }
    public double getPe20() { return pe20; }
    public MetodologiaTasacion getMetodologia() { return metodologia; }
    public LocalDate getFecha() { return fecha; }
    public String getModelVersion() { return modelVersion; }
    public long getVersion() { return version; }
    public SyncState getSyncState() { return syncState; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setVersion(long version) { this.version = version; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
