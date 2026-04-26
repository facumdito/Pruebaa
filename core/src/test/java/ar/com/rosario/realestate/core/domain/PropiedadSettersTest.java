package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.TenantId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Propiedad setters used during reconstruction from persistence.
 */
class PropiedadSettersTest {

    private Propiedad unaPropiedad() {
        return new Propiedad(
            PropiedadId.newId(), TenantId.of("t-001"),
            "San Martín 456", TipoPropiedad.CASA, OperacionPropiedad.AMBOS
        );
    }

    @Test
    void settersDetallesSinIncrementarVersion() {
        Propiedad p = unaPropiedad();

        p.setM2Cubiertos(new BigDecimal("75.00"));
        p.setM2Totales(new BigDecimal("90.00"));
        p.setAmbientes(3);
        p.setDormitorios(2);
        p.setBanos(1);
        p.setCocheras(1);
        p.setAntiguedadAnios(10);
        p.setDescripcion("Luminosa, bien ubicada");

        assertEquals(new BigDecimal("75.00"), p.getM2Cubiertos());
        assertEquals(new BigDecimal("90.00"), p.getM2Totales());
        assertEquals(3, p.getAmbientes());
        assertEquals(2, p.getDormitorios());
        assertEquals(1, p.getBanos());
        assertEquals(1, p.getCocheras());
        assertEquals(10, p.getAntiguedadAnios());
        assertEquals("Luminosa, bien ubicada", p.getDescripcion());
        assertEquals(0L, p.getVersion());
    }

    @Test
    void idYTenantSonInmutables() {
        PropiedadId id = PropiedadId.newId();
        TenantId tid = TenantId.of("t-002");
        Propiedad p = new Propiedad(id, tid, "Pellegrini 789",
                                    TipoPropiedad.LOCAL, OperacionPropiedad.ALQUILER);
        assertSame(id, p.getId());
        assertSame(tid, p.getTenantId());
    }

    @Test
    void flujoCompletoConMultiplesModificaciones() {
        Propiedad p = unaPropiedad();

        p.setBarrio(Barrio.of("Alberdi"));
        p.setCoordenadas(new Coordenadas(-32.95, -60.65));
        p.setAmbientes(4);
        p.actualizarPrecio(new BigDecimal("85000"), new BigDecimal("1250"), java.time.LocalDate.now());
        p.cambiarEstado(EstadoPropiedad.RESERVADA);

        assertEquals(2L, p.getVersion()); // actualizarPrecio + cambiarEstado
        assertEquals(EstadoPropiedad.RESERVADA, p.getEstado());
        assertEquals(4, p.getAmbientes());
    }
}
