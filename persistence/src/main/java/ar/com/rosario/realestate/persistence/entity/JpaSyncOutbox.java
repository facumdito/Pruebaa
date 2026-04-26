package ar.com.rosario.realestate.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "sync_outbox",
    indexes = {
        @Index(name = "idx_outbox_pending", columnList = "tenant_id,processed_at,attempts")
    }
)
public class JpaSyncOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", length = 36, nullable = false)
    private String tenantId;

    @Column(name = "entity_type", length = 64, nullable = false)
    private String entityType;

    @Column(name = "entity_id", length = 36, nullable = false)
    private String entityId;

    @Column(name = "op", nullable = false)
    @Enumerated(EnumType.STRING)
    private Op op;

    @Column(name = "payload", nullable = false, columnDefinition = "JSON")
    private String payload;

    @Column(name = "base_version", nullable = false)
    private Long baseVersion;

    @Column(name = "idempotency_key", length = 128, nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "processed_at")
    private Instant processedAt;

    public enum Op { INSERT, UPDATE, DELETE }

    public Long getId() { return id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String t) { this.tenantId = t; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String t) { this.entityType = t; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String i) { this.entityId = i; }
    public Op getOp() { return op; }
    public void setOp(Op op) { this.op = op; }
    public String getPayload() { return payload; }
    public void setPayload(String p) { this.payload = p; }
    public Long getBaseVersion() { return baseVersion; }
    public void setBaseVersion(Long v) { this.baseVersion = v; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String k) { this.idempotencyKey = k; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant t) { this.createdAt = t; }
    public int getAttempts() { return attempts; }
    public void setAttempts(int a) { this.attempts = a; }
    public String getLastError() { return lastError; }
    public void setLastError(String e) { this.lastError = e; }
    public Instant getProcessedAt() { return processedAt; }
    public void setProcessedAt(Instant t) { this.processedAt = t; }
}
