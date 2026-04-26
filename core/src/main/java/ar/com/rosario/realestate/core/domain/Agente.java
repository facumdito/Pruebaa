package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;

import java.time.Instant;
import java.util.Objects;

public class Agente {

    private final AgenteId id;
    private final TenantId tenantId;

    private String nombre;
    private String email;
    private String telefono;
    private boolean activo;

    private long version;
    private SyncState syncState;
    private Instant createdAt;
    private Instant updatedAt;

    public Agente(AgenteId id, TenantId tenantId, String nombre, String email) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        Objects.requireNonNull(nombre, "nombre must not be null");
        this.id = id;
        this.tenantId = tenantId;
        this.nombre = nombre;
        this.email = email;
        this.activo = true;
        this.version = 0;
        this.syncState = SyncState.LOCAL;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void desactivar() {
        this.activo = false;
        touch();
    }

    public void activar() {
        this.activo = true;
        touch();
    }

    public void actualizarContacto(String email, String telefono) {
        this.email = email;
        this.telefono = telefono;
        touch();
    }

    private void touch() {
        this.version++;
        this.updatedAt = Instant.now();
        if (this.syncState == SyncState.SYNCED) this.syncState = SyncState.LOCAL;
    }

    public AgenteId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public boolean isActivo() { return activo; }
    public long getVersion() { return version; }
    public SyncState getSyncState() { return syncState; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setVersion(long version) { this.version = version; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
