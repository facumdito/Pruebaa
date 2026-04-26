package ar.com.rosario.realestate.persistence;

import ar.com.rosario.realestate.core.domain.*;
import ar.com.rosario.realestate.core.port.out.PropiedadRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = PersistenceTestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PropiedadRepositoryIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4")
        .withDatabaseName("rosario_re_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("TEST_DB_URL", mysql::getJdbcUrl);
    }

    @Autowired PropiedadRepository repo;

    static final TenantId TENANT = TenantId.of("tenant-test-01");
    static PropiedadId savedId;

    @BeforeEach
    void setTenant() { TenantContext.set(TENANT.value()); }

    @AfterEach
    void clearTenant() { TenantContext.clear(); }

    @Test @Order(1)
    void save_and_findById() {
        Propiedad p = new Propiedad(
            PropiedadId.newId(), TENANT,
            "San Martín 1234, Rosario",
            TipoPropiedad.DEPARTAMENTO,
            OperacionPropiedad.VENTA
        );
        p.actualizarPrecio(BigDecimal.valueOf(85000), BigDecimal.valueOf(990), LocalDate.now());
        repo.save(p);
        savedId = p.getId();

        Optional<Propiedad> found = repo.findById(TENANT, savedId);
        assertThat(found).isPresent();
        assertThat(found.get().getDireccion()).isEqualTo("San Martín 1234, Rosario");
        assertThat(found.get().getPrecioUsd()).isEqualByComparingTo(BigDecimal.valueOf(85000));
    }

    @Test @Order(2)
    void findByTenant_returns_only_own_tenant() {
        List<Propiedad> list = repo.findByTenant(TENANT);
        assertThat(list).isNotEmpty();
        assertThat(list).allMatch(p -> p.getTenantId().equals(TENANT));
    }

    @Test @Order(3)
    void findByTenantAndEstado_filters_correctly() {
        List<Propiedad> disponibles = repo.findByTenantAndEstado(TENANT, EstadoPropiedad.DISPONIBLE);
        assertThat(disponibles).isNotEmpty();
        assertThat(disponibles).allMatch(p -> p.getEstado() == EstadoPropiedad.DISPONIBLE);
    }

    @Test @Order(4)
    void delete_soft_deletes() {
        repo.delete(TENANT, savedId);
        Optional<Propiedad> found = repo.findById(TENANT, savedId);
        assertThat(found).isEmpty();
    }
}
