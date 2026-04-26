package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;

import java.time.Instant;

public class Lead {

    private static final double W_BUDGET    = 0.30;
    private static final double W_AUTHORITY = 0.15;
    private static final double W_NEED      = 0.25;
    private static final double W_TIMELINE  = 0.30;

    private final LeadId id;
    private final TenantId tenantId;

    private String nombre;
    private String telefono;
    private String email;
    private FuenteLead fuente;
    private EstadoLead estado;

    // BANT scoring (Budget 30%, Authority 15%, Need 25%, Timeline 30%)
    private int bantBudget;
    private int bantAuthority;
    private int bantNeed;
    private int bantTimeline;
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
        this.bantBudget = 0;
        this.bantAuthority = 0;
        this.bantNeed = 0;
        this.bantTimeline = 0;
        this.score = 0;
        this.version = 0;
        this.syncState = SyncState.LOCAL;
        this.deleted = false;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * Updates BANT criteria and recalculates weighted score.
     * Budget 30% + Authority 15% + Need 25% + Timeline 30% = 100%.
     */
    public void actualizarBant(int budget, int authority, int need, int timeline) {
        validateFactor("budget", budget);
        validateFactor("authority", authority);
        validateFactor("need", need);
        validateFactor("timeline", timeline);
        this.bantBudget = budget;
        this.bantAuthority = authority;
        this.bantNeed = need;
        this.bantTimeline = timeline;
        this.score = calcularScoreBant();
        touch();
    }

    /** Manual score override when BANT is not applicable. */
    public void actualizarScore(int score) {
        if (score < 0 || score > 100) throw new IllegalArgumentException("score must be 0-100");
        this.score = score;
        touch();
    }

    public void avanzarEstado(EstadoLead nuevoEstado) {
        this.estado = nuevoEstado;
        touch();
    }

    public void marcarEliminado() {
        this.deleted = true;
        touch();
    }

    private int calcularScoreBant() {
        return (int) Math.round(
            bantBudget    * W_BUDGET
          + bantAuthority * W_AUTHORITY
          + bantNeed      * W_NEED
          + bantTimeline  * W_TIMELINE
        );
    }

    private static void validateFactor(String name, int value) {
        if (value < 0 || value > 100)
            throw new IllegalArgumentException("BANT " + name + " must be 0-100, got " + value);
    }

    private void touch() {
        this.version++;
        this.updatedAt = Instant.now();
        if (this.syncState == SyncState.SYNCED) this.syncState = SyncState.LOCAL;
    }

    public LeadId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public FuenteLead getFuente() { return fuente; }
    public EstadoLead getEstado() { return estado; }
    public int getBantBudget() { return bantBudget; }
    public int getBantAuthority() { return bantAuthority; }
    public int getBantNeed() { return bantNeed; }
    public int getBantTimeline() { return bantTimeline; }
    public int getScore() { return score; }
    public String getNotas() { return notas; }
    public long getVersion() { return version; }
    public SyncState getSyncState() { return syncState; }
    public boolean isDeleted() { return deleted; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setNotas(String notas) { this.notas = notas; }
    public void setVersion(long version) { this.version = version; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
