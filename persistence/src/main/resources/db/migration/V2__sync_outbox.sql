-- ============================================================
-- V2: Sync outbox for local-first offline replication
-- ============================================================

CREATE TABLE sync_outbox (
    id              BIGINT AUTO_INCREMENT                      NOT NULL,
    tenant_id       CHAR(36)                                   NOT NULL,
    entity_type     VARCHAR(64)                                NOT NULL,
    entity_id       CHAR(36)                                   NOT NULL,
    op              ENUM('INSERT','UPDATE','DELETE')            NOT NULL,
    payload         JSON                                       NOT NULL,
    base_version    BIGINT                                     NOT NULL,
    idempotency_key VARCHAR(128)                               NOT NULL,
    created_at      DATETIME(3)                                NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    attempts        INT                                        NOT NULL DEFAULT 0,
    last_error      TEXT                                       NULL,
    processed_at    DATETIME(3)                                NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_idempotency (idempotency_key),
    INDEX idx_outbox_pending (tenant_id, processed_at, attempts)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
