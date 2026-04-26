package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.TenantId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TasacionTest {

    private static final TenantId TENANT = TenantId.of("t-001");

    @Test
    void constructorEstableceValoresIniciales() {
        Tasacion t = new Tasacion(
            TasacionId.newId(), TENANT, PropiedadId.newId(),
            AgenteId.newId(), MetodologiaTasacion.MANUAL
        );
        assertEquals(MetodologiaTasacion.MANUAL, t.getMetodologia());
        assertEquals(0L, t.getVersion());
        assertNotNull(t.getFecha());
        assertNotNull(t.getCreatedAt());
    }

    @Test
    void desdeResultadoAvmCopiaTodasLasMetricas() {
        PropiedadId propId = PropiedadId.newId();
        TasacionResult resultado = new TasacionResult(
            propId,
            new BigDecimal("120000.00"),
            new BigDecimal("108000.00"),
            new BigDecimal("132000.00"),
            0.095,
            0.72,
            0.88,
            "pmml-v1",
            Instant.now()
        );

        Tasacion t = Tasacion.desdeResultadoAvm(
            TasacionId.newId(), TENANT, AgenteId.newId(), resultado
        );

        assertEquals(propId, t.getPropiedadId());
        assertEquals(new BigDecimal("120000.00"), t.getValorEstimadoUsd());
        assertEquals(new BigDecimal("108000.00"), t.getBandaInferiorUsd());
        assertEquals(new BigDecimal("132000.00"), t.getBandaSuperiorUsd());
        assertEquals(0.095, t.getMdape());
        assertEquals(0.72, t.getPe10());
        assertEquals(0.88, t.getPe20());
        assertEquals("pmml-v1", t.getModelVersion());
        assertEquals(MetodologiaTasacion.AVM_PMML, t.getMetodologia());
    }

    @Test
    void isHighConfidenceTrueConPe10SobreSetenta() {
        PropiedadId propId = PropiedadId.newId();
        TasacionResult resultado = new TasacionResult(
            propId, BigDecimal.valueOf(100000), BigDecimal.valueOf(90000),
            BigDecimal.valueOf(110000), 0.08, 0.75, 0.90, "v1", Instant.now()
        );
        Tasacion t = Tasacion.desdeResultadoAvm(TasacionId.newId(), TENANT, AgenteId.newId(), resultado);
        assertTrue(t.isHighConfidence());
    }

    @Test
    void isHighConfidenceFalseConPe10BajoSetenta() {
        PropiedadId propId = PropiedadId.newId();
        TasacionResult resultado = new TasacionResult(
            propId, BigDecimal.valueOf(100000), BigDecimal.valueOf(85000),
            BigDecimal.valueOf(115000), 0.14, 0.60, 0.80, "v1", Instant.now()
        );
        Tasacion t = Tasacion.desdeResultadoAvm(TasacionId.newId(), TENANT, AgenteId.newId(), resultado);
        assertFalse(t.isHighConfidence());
    }
}
