-- ============================================================
-- V1: Initial multi-tenant schema
-- Every business table carries tenant_id.
-- PKs are (tenant_id, id) so InnoDB clusters rows by tenant.
-- ============================================================

CREATE TABLE tenants (
    id         CHAR(36)                                         NOT NULL,
    nombre     VARCHAR(255)                                     NOT NULL,
    plan       ENUM('STARTER', 'PRO', 'PREMIUM')                NOT NULL DEFAULT 'STARTER',
    activo     TINYINT(1)                                       NOT NULL DEFAULT 1,
    created_at DATETIME(3)                                      NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3)                                      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------

CREATE TABLE propiedades (
    id               CHAR(36)                                                                        NOT NULL,
    tenant_id        CHAR(36)                                                                        NOT NULL,
    direccion        VARCHAR(255)                                                                    NOT NULL,
    barrio           VARCHAR(100)                                                                    NULL,
    tipo             ENUM('DEPARTAMENTO','CASA','PH','LOCAL','COCHERA','TERRENO','OFICINA')           NOT NULL,
    operacion        ENUM('VENTA','ALQUILER','AMBOS')                                               NOT NULL,
    estado           ENUM('DISPONIBLE','RESERVADA','VENDIDA','ALQUILADA','PAUSADA')                  NOT NULL DEFAULT 'DISPONIBLE',
    latitud          DECIMAL(10, 7)                                                                  NULL,
    longitud         DECIMAL(10, 7)                                                                  NULL,
    precio_usd       DECIMAL(15, 2)                                                                  NULL,
    tipo_cambio_mep  DECIMAL(15, 4)                                                                  NULL,
    fecha_cotizacion DATE                                                                            NULL,
    m2_cubiertos     DECIMAL(8, 2)                                                                   NULL,
    m2_totales       DECIMAL(8, 2)                                                                   NULL,
    ambientes        TINYINT                                                                         NULL,
    dormitorios      TINYINT                                                                         NULL,
    banos            TINYINT                                                                         NULL,
    cocheras         TINYINT                                                                         NULL,
    antiguedad_anios SMALLINT                                                                        NULL,
    descripcion      TEXT                                                                            NULL,
    version          BIGINT                                                                          NOT NULL DEFAULT 0,
    sync_state       ENUM('LOCAL','SYNCED','CONFLICT')                                               NOT NULL DEFAULT 'LOCAL',
    deleted          TINYINT(1)                                                                      NOT NULL DEFAULT 0,
    created_at       DATETIME(3)                                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at       DATETIME(3)                                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (tenant_id, id),
    INDEX idx_prop_tenant_updated (tenant_id, updated_at),
    INDEX idx_prop_estado (tenant_id, estado),
    INDEX idx_prop_barrio (tenant_id, barrio),
    CONSTRAINT fk_prop_tenant FOREIGN KEY (tenant_id) REFERENCES tenants (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ------------------------------------------------------------

CREATE TABLE leads (
    id         CHAR(36)                                                                                         NOT NULL,
    tenant_id  CHAR(36)                                                                                         NOT NULL,
    nombre     VARCHAR(255)                                                                                     NOT NULL,
    fuente     ENUM('WHATSAPP','WEB','PORTAL','REFERIDO','MANUAL')                                              NOT NULL DEFAULT 'MANUAL',
    estado     ENUM('NUEVO','CALIFICADO','VISITA_AGENDADA','VISITA_REALIZADA','OFERTA','CERRADO','PERDIDO')      NOT NULL DEFAULT 'NUEVO',
    score      TINYINT                                                                                          NOT NULL DEFAULT 0,
    notas      TEXT                                                                                             NULL,
    version    BIGINT                                                                                           NOT NULL DEFAULT 0,
    sync_state ENUM('LOCAL','SYNCED','CONFLICT')                                                                NOT NULL DEFAULT 'LOCAL',
    deleted    TINYINT(1)                                                                                       NOT NULL DEFAULT 0,
    created_at DATETIME(3)                                                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3)                                                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (tenant_id, id),
    INDEX idx_lead_tenant_estado (tenant_id, estado),
    CONSTRAINT fk_lead_tenant FOREIGN KEY (tenant_id) REFERENCES tenants (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
