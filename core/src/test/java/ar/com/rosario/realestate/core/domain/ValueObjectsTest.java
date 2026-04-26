package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.TenantId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for records, IDs, commands, and enums that would otherwise have zero coverage.
 */
class ValueObjectsTest {

    // ---- IDs ----

    @Test
    void propiedadIdOfYNewIdFuncionan() {
        PropiedadId a = PropiedadId.newId();
        PropiedadId b = PropiedadId.of(a.value());
        assertEquals(a, b);
        assertNotEquals(a, PropiedadId.newId());
    }

    @Test
    void leadIdOfYNewIdFuncionan() {
        LeadId a = LeadId.newId();
        LeadId b = LeadId.of(a.value());
        assertEquals(a, b);
    }

    @Test
    void agenteIdOfYNewIdFuncionan() {
        AgenteId a = AgenteId.newId();
        AgenteId b = AgenteId.of(a.value());
        assertEquals(a, b);
    }

    @Test
    void visitaIdOfYNewIdFuncionan() {
        VisitaId a = VisitaId.newId();
        VisitaId b = VisitaId.of(a.value());
        assertEquals(a, b);
    }

    @Test
    void tasacionIdOfYNewIdFuncionan() {
        TasacionId a = TasacionId.newId();
        TasacionId b = TasacionId.of(a.value());
        assertEquals(a, b);
    }

    @Test
    void tenantIdNuloLanza() {
        assertThrows(NullPointerException.class, () -> TenantId.of(null));
    }

    @Test
    void tenantIdBlancoLanza() {
        assertThrows(IllegalArgumentException.class, () -> TenantId.of("  "));
    }

    @Test
    void tenantIdNewIdEsUuid() {
        TenantId id = TenantId.newId();
        assertNotNull(id.value());
        assertEquals(36, id.value().length()); // UUID length
    }

    // ---- Barrio ----

    @Test
    void barrioOfTrimsEspacios() {
        Barrio b = Barrio.of("  Pichincha  ");
        assertEquals("Pichincha", b.nombre());
    }

    @Test
    void barrioNombreBlankLanza() {
        assertThrows(IllegalArgumentException.class, () -> Barrio.of("   "));
    }

    @Test
    void barrioNombreNuloLanza() {
        assertThrows(NullPointerException.class, () -> new Barrio(null));
    }

    // ---- TasacionResult record ----

    @Test
    void tasacionResultIsHighConfidence() {
        TasacionResult r = new TasacionResult(
            PropiedadId.newId(), BigDecimal.valueOf(100000),
            BigDecimal.valueOf(90000), BigDecimal.valueOf(110000),
            0.09, 0.71, 0.89, "v1", Instant.now()
        );
        assertTrue(r.isHighConfidence());
    }

    @Test
    void tasacionResultIsNotHighConfidence() {
        TasacionResult r = new TasacionResult(
            PropiedadId.newId(), BigDecimal.valueOf(100000),
            BigDecimal.valueOf(80000), BigDecimal.valueOf(120000),
            0.14, 0.65, 0.82, "v1", Instant.now()
        );
        assertFalse(r.isHighConfidence());
    }

    // ---- Command records ----

    @Test
    void tasarPropiedadCommandInstancia() {
        TenantId tid = TenantId.of("t-1");
        PropiedadId pid = PropiedadId.newId();
        var cmd = new ar.com.rosario.realestate.core.port.in.TasarPropiedadCommand(tid, pid);
        assertEquals(tid, cmd.tenantId());
        assertEquals(pid, cmd.propiedadId());
    }

    @Test
    void agendarVisitaCommandInstancia() {
        TenantId tid = TenantId.of("t-1");
        PropiedadId pid = PropiedadId.newId();
        LeadId lid = LeadId.newId();
        AgenteId aid = AgenteId.newId();
        Instant now = Instant.now();
        var cmd = new ar.com.rosario.realestate.core.port.in.AgendarVisitaCommand(
            tid, pid, lid, aid, now, 60);
        assertEquals(tid, cmd.tenantId());
        assertEquals(pid, cmd.propiedadId());
        assertEquals(60, cmd.duracionMinutos());
    }

    @Test
    void registrarLeadCommandInstancia() {
        TenantId tid = TenantId.of("t-1");
        var cmd = new ar.com.rosario.realestate.core.port.in.RegistrarLeadCommand(
            tid, "Juan", "341123456", "juan@mail.com", FuenteLead.WHATSAPP);
        assertEquals("Juan", cmd.nombre());
        assertEquals(FuenteLead.WHATSAPP, cmd.fuente());
    }

    // ---- Enums ----

    @Test
    void metodologiaTasacionValues() {
        assertEquals(4, MetodologiaTasacion.values().length);
        assertEquals(MetodologiaTasacion.AVM_PMML, MetodologiaTasacion.valueOf("AVM_PMML"));
        assertEquals(MetodologiaTasacion.MANUAL, MetodologiaTasacion.valueOf("MANUAL"));
    }

    @Test
    void estadoVisitaValues() {
        assertEquals(5, EstadoVisita.values().length);
    }

    @Test
    void tiposYOperaciones() {
        assertEquals(7, TipoPropiedad.values().length);
        assertEquals(3, OperacionPropiedad.values().length);
        assertEquals(5, EstadoPropiedad.values().length);
        assertEquals(7, EstadoLead.values().length);
        assertEquals(5, FuenteLead.values().length);
    }
}
