package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;

import java.time.Instant;

public class Lead {

    private final LeadId id;
    private final TenantId tenantId;

    private String nombre;
    private String telefono;
    private String email;
    private FuenteLead fuente;
    private EstadoLead estado;
    private int score;
    private String notas;

    private long version;
    private SyncState syncState;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;

    public Lead(LeadId id, TenantId tenantId, String nombre, FuenteLead fuente) {
        this.id = id;
        this.tenantId = tenantId;
        this.nombre = nombre;
        this.fuente = fuente;
        this.estado = EstadoLead.NUEVO;
        this.score = 0;
        this.version = 0;
        this.syncState = SyncState.LOCAL;
        this.deleted = false;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void avanzarEstado(EstadoLead nuevoEstado) {
        this.estado = nuevoEstado;
        touch();
    }

    public void actualizarScore(int score) {
        if (score < 0 || score > 100) throw new IllegalArgumentException("score must be 0-100");
        this.score = score;
        touch();
    }

    private void touch() {
        this.version++;
        this.updatedAt = Instant.now();
        if (this.syncState == SyncState.SYNCED) this.syncState = SyncState.LOCAL;
    }

    // Getters
    public LeadId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public FuenteLead getFuente() { return fuente; }
    public EstadoLead getEstado() { return estado; }
    public int getScore() { return score; }
    public String getNotas() { return notas; }
    public long getVersion() { return version; }
    public SyncState getSyncState() { return syncState; }
    public boolean isDeleted() { return deleted; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters for reconstruction
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setNotas(String notas) { this.notas = notas; }
    public void setVersion(long version) { this.version = version; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
