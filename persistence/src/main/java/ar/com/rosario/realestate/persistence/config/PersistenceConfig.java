package ar.com.rosario.realestate.persistence.config;

import ar.com.rosario.realestate.persistence.tenant.TenantIdentifierResolver;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ar.com.rosario.realestate.persistence")
public class PersistenceConfig {

    @Bean
    public HibernatePropertiesCustomizer multiTenancyCustomizer(TenantIdentifierResolver resolver) {
        return properties ->
                properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, resolver);
    }
}
