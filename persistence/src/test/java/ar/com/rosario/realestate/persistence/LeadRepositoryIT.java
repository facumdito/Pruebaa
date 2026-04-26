package ar.com.rosario.realestate.persistence;

import ar.com.rosario.realestate.core.domain.*;
import ar.com.rosario.realestate.core.port.out.LeadRepository;
import ar.com.rosario.realestate.shared.TenantContext;
import ar.com.rosario.realestate.shared.TenantId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
class LeadRepositoryIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4")
        .withDatabaseName("rosario_re_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("TEST_DB_URL", mysql::getJdbcUrl);
    }

    @Autowired LeadRepository repo;

    static final TenantId TENANT = TenantId.of("tenant-lead-test");

    @BeforeEach void setTenant()   { TenantContext.set(TENANT.value()); }
    @AfterEach  void clearTenant() { TenantContext.clear(); }

    @Test
    void registrar_y_buscar_por_estado() {
        Lead lead = new Lead(LeadId.newId(), TENANT, "Carlos Pérez", FuenteLead.WHATSAPP);
        lead.setTelefono("+5493412345678");
        lead.setEmail("carlos@example.com");
        repo.save(lead);

        List<Lead> nuevos = repo.findByTenantAndEstado(TENANT, EstadoLead.NUEVO);
        assertThat(nuevos).anyMatch(l -> l.getNombre().equals("Carlos Pérez"));
    }

    @Test
    void bant_score_persisted() {
        Lead lead = new Lead(LeadId.newId(), TENANT, "María García", FuenteLead.WEB);
        lead.actualizarBant(80, 70, 90, 85);
        repo.save(lead);

        var found = repo.findById(TENANT, lead.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getScore()).isGreaterThan(0);
        assertThat(found.get().getBantBudget()).isEqualTo(80);
    }

    @Test
    void soft_delete_oculta_lead() {
        Lead lead = new Lead(LeadId.newId(), TENANT, "Lead a borrar", FuenteLead.MANUAL);
        repo.save(lead);

        lead.marcarEliminado();
        repo.save(lead);

        var found = repo.findById(TENANT, lead.getId());
        assertThat(found).isEmpty();
    }
}
