package ar.com.rosario.realestate.persistence.config;

import org.springframework.context.annotation.Configuration;

/**
 * JPA multi-tenant configuration — wired in Phase 2.
 * Will configure CurrentTenantIdentifierResolver and MultiTenantConnectionProvider
 * using Hibernate @TenantId discriminator on every business entity.
 */
@Configuration
public class PersistenceConfig {
}
