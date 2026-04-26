-- ============================================================
-- V3: Agentes, visitas, tasaciones
-- All FKs include tenant_id — no cross-tenant references.
-- ============================================================

CREATE TABLE agentes (
    id         CHAR(36)    NOT NULL,
    tenant_id  CHAR(36)    NOT NULL,
    nombre     VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NULL,
    telefono   VARCHAR(50)  NULL,
    activo     TINYINT(1)   NOT NULL DEFAULT 1,
    version    BIGINT       NOT NULL DEFAULT 0,
    sync_state ENUM('LOCAL','SYNCED','CONFLICT') NOT NULL DEFAULT 'LOCAL',
    created_at DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (tenant_id, id),
    INDEX idx_agente_activo (tenant_id, activo),
    CONSTRAINT fk_agente_tenant FOREIGN KEY (tenant_id) REFERENCES tenants (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------

CREATE TABLE visitas (
    id               CHAR(36)                                                NOT NULL,
    tenant_id        CHAR(36)                                                NOT NULL,
    propiedad_id     CHAR(36)                                                NOT NULL,
    lead_id          CHAR(36)                                                NOT NULL,
    agente_id        CHAR(36)                                                NULL,
    fecha_hora       DATETIME(3)                                             NOT NULL,
    duracion_minutos INT                                                     NOT NULL DEFAULT 60,
    estado           ENUM('AGENDADA','CONFIRMADA','REALIZADA','CANCELADA','NO_SHOW') NOT NULL DEFAULT 'AGENDADA',
    notas            TEXT                                                    NULL,
    version          BIGINT                                                  NOT NULL DEFAULT 0,
    sync_state       ENUM('LOCAL','SYNCED','CONFLICT')                       NOT NULL DEFAULT 'LOCAL',
    created_at       DATETIME(3)                                             NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at       DATETIME(3)                                             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (tenant_id, id),
    INDEX idx_visita_estado (tenant_id, estado),
    INDEX idx_visita_fecha (tenant_id, fecha_hora),
    INDEX idx_visita_agente (tenant_id, agente_id),
    CONSTRAINT fk_visita_tenant    FOREIGN KEY (tenant_id)              REFERENCES tenants     (id),
    CONSTRAINT fk_visita_propiedad FOREIGN KEY (tenant_id, propiedad_id) REFERENCES propiedades (tenant_id, id),
    CONSTRAINT fk_visita_lead      FOREIGN KEY (tenant_id, lead_id)      REFERENCES leads       (tenant_id, id),
    CONSTRAINT fk_visita_agente    FOREIGN KEY (tenant_id, agente_id)    REFERENCES agentes     (tenant_id, id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------

CREATE TABLE tasaciones (
    id                  CHAR(36)                                          NOT NULL,
    tenant_id           CHAR(36)                                          NOT NULL,
    propiedad_id        CHAR(36)                                          NOT NULL,
    tasador_id          CHAR(36)                                          NULL,
    valor_estimado_usd  DECIMAL(15, 2)                                    NULL,
    banda_inferior_usd  DECIMAL(15, 2)                                    NULL,
    banda_superior_usd  DECIMAL(15, 2)                                    NULL,
    mdape               DECIMAL(6, 4)                                     NULL,
    pe10                DECIMAL(6, 4)                                     NULL,
    pe20                DECIMAL(6, 4)                                     NULL,
    metodologia         ENUM('AVM_PMML','COMPARABLE_KNN','MANUAL','MIXTA') NOT NULL DEFAULT 'MANUAL',
    fecha               DATE                                              NOT NULL,
    model_version       VARCHAR(64)                                       NULL,
    version             BIGINT                                            NOT NULL DEFAULT 0,
    sync_state          ENUM('LOCAL','SYNCED','CONFLICT')                 NOT NULL DEFAULT 'LOCAL',
    created_at          DATETIME(3)                                       NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at          DATETIME(3)                                       NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (tenant_id, id),
    INDEX idx_tasacion_propiedad (tenant_id, propiedad_id, fecha DESC),
    CONSTRAINT fk_tasacion_tenant    FOREIGN KEY (tenant_id)               REFERENCES tenants     (id),
    CONSTRAINT fk_tasacion_propiedad FOREIGN KEY (tenant_id, propiedad_id)  REFERENCES propiedades (tenant_id, id),
    CONSTRAINT fk_tasacion_tasador   FOREIGN KEY (tenant_id, tasador_id)    REFERENCES agentes     (tenant_id, id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
