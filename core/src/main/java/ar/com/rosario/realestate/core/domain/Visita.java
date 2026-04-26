package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;

import java.time.Instant;
import java.util.Objects;

public class Visita {

    private final VisitaId id;
    private final TenantId tenantId;
    private final PropiedadId propiedadId;
    private final LeadId leadId;

    private AgenteId agenteId;
    private Instant fechaHora;
    private int duracionMinutos;
    private EstadoVisita estado;
    private String notas;

    private long version;
    private SyncState syncState;
    private Instant createdAt;
    private Instant updatedAt;

    public Visita(VisitaId id, TenantId tenantId, PropiedadId propiedadId,
                  LeadId leadId, AgenteId agenteId, Instant fechaHora, int duracionMinutos) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        Objects.requireNonNull(propiedadId, "propiedadId must not be null");
        Objects.requireNonNull(leadId, "leadId must not be null");
        Objects.requireNonNull(fechaHora, "fechaHora must not be null");
        if (duracionMinutos <= 0) throw new IllegalArgumentException("duracionMinutos must be > 0");
        this.id = id;
        this.tenantId = tenantId;
        this.propiedadId = propiedadId;
        this.leadId = leadId;
        this.agenteId = agenteId;
        this.fechaHora = fechaHora;
        this.duracionMinutos = duracionMinutos;
        this.estado = EstadoVisita.AGENDADA;
        this.version = 0;
        this.syncState = SyncState.LOCAL;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void confirmar() {
        requireEstado(EstadoVisita.AGENDADA, "confirmar");
        this.estado = EstadoVisita.CONFIRMADA;
        touch();
    }

    public void realizar(String notas) {
        if (this.estado == EstadoVisita.CANCELADA || this.estado == EstadoVisita.NO_SHOW) {
            throw new IllegalStateException("No se puede realizar una visita " + this.estado);
        }
        this.estado = EstadoVisita.REALIZADA;
        this.notas = notas;
        touch();
    }

    public void cancelar(String motivo) {
        if (this.estado == EstadoVisita.REALIZADA) {
            throw new IllegalStateException("No se puede cancelar una visita ya realizada");
        }
        this.estado = EstadoVisita.CANCELADA;
        this.notas = motivo;
        touch();
    }

    public void marcarNoShow() {
        requireEstado(EstadoVisita.CONFIRMADA, "marcar no-show");
        this.estado = EstadoVisita.NO_SHOW;
        touch();
    }

    public void reprogramar(Instant nuevaFechaHora, AgenteId nuevoAgente) {
        if (this.estado == EstadoVisita.REALIZADA || this.estado == EstadoVisita.CANCELADA) {
            throw new IllegalStateException("No se puede reprogramar una visita " + this.estado);
        }
        this.fechaHora = Objects.requireNonNull(nuevaFechaHora);
        if (nuevoAgente != null) this.agenteId = nuevoAgente;
        this.estado = EstadoVisita.AGENDADA;
        touch();
    }

    private void requireEstado(EstadoVisita required, String action) {
        if (this.estado != required) {
            throw new IllegalStateException(
                "Para " + action + " la visita debe estar " + required + ", está " + this.estado);
        }
    }

    private void touch() {
        this.version++;
        this.updatedAt = Instant.now();
        if (this.syncState == SyncState.SYNCED) this.syncState = SyncState.LOCAL;
    }

    public VisitaId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public PropiedadId getPropiedadId() { return propiedadId; }
    public LeadId getLeadId() { return leadId; }
    public AgenteId getAgenteId() { return agenteId; }
    public Instant getFechaHora() { return fechaHora; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public EstadoVisita getEstado() { return estado; }
    public String getNotas() { return notas; }
    public long getVersion() { return version; }
    public SyncState getSyncState() { return syncState; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setVersion(long version) { this.version = version; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
