package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PropiedadTest {

    private Propiedad unaPropiedad() {
        return new Propiedad(
            PropiedadId.newId(),
            TenantId.of("t-001"),
            "Córdoba 1234",
            TipoPropiedad.DEPARTAMENTO,
            OperacionPropiedad.VENTA
        );
    }

    @Test
    void constructorEstableceValoresIniciales() {
        Propiedad p = unaPropiedad();
        assertEquals(EstadoPropiedad.DISPONIBLE, p.getEstado());
        assertEquals(SyncState.LOCAL, p.getSyncState());
        assertEquals(0L, p.getVersion());
        assertFalse(p.isDeleted());
        assertNotNull(p.getCreatedAt());
        assertNotNull(p.getUpdatedAt());
    }

    @Test
    void actualizarPrecioGuardaLosTresCampos() {
        Propiedad p = unaPropiedad();
        BigDecimal usd = new BigDecimal("120000.00");
        BigDecimal mep = new BigDecimal("1250.5000");
        LocalDate fecha = LocalDate.of(2026, 4, 26);

        p.actualizarPrecio(usd, mep, fecha);

        assertEquals(usd, p.getPrecioUsd());
        assertEquals(mep, p.getTipoCambioMep());
        assertEquals(fecha, p.getFechaCotizacion());
        assertEquals(1L, p.getVersion());
    }

    @Test
    void actualizarPrecioIncrementaVersionYMarcaLocal() {
        Propiedad p = unaPropiedad();
        p.setSyncState(SyncState.SYNCED);

        p.actualizarPrecio(BigDecimal.valueOf(100000), BigDecimal.valueOf(1200), LocalDate.now());

        assertEquals(SyncState.LOCAL, p.getSyncState());
        assertEquals(1L, p.getVersion());
    }

    @Test
    void cambiarEstadoActualizaCorrectamente() {
        Propiedad p = unaPropiedad();
        p.cambiarEstado(EstadoPropiedad.RESERVADA);

        assertEquals(EstadoPropiedad.RESERVADA, p.getEstado());
        assertEquals(1L, p.getVersion());
    }

    @Test
    void cambiarEstadoVendidaFlujoCompleto() {
        Propiedad p = unaPropiedad();
        p.cambiarEstado(EstadoPropiedad.RESERVADA);
        p.cambiarEstado(EstadoPropiedad.VENDIDA);

        assertEquals(EstadoPropiedad.VENDIDA, p.getEstado());
        assertEquals(2L, p.getVersion());
    }

    @Test
    void marcarEliminadaSeteaDeletedTrue() {
        Propiedad p = unaPropiedad();
        p.marcarEliminada();

        assertTrue(p.isDeleted());
        assertEquals(1L, p.getVersion());
    }

    @Test
    void setBarrioYCoordenadasNoIncrementanVersion() {
        Propiedad p = unaPropiedad();
        p.setBarrio(Barrio.of("Pichincha"));
        p.setCoordenadas(new Coordenadas(-32.9442, -60.6505));

        assertEquals(Barrio.of("Pichincha"), p.getBarrio());
        assertNotNull(p.getCoordenadas());
        assertEquals(0L, p.getVersion());
    }

    @Test
    void versionSincronizadaVuelveALocalAlModificar() {
        Propiedad p = unaPropiedad();
        p.setSyncState(SyncState.SYNCED);
        assertEquals(SyncState.SYNCED, p.getSyncState());

        p.cambiarEstado(EstadoPropiedad.PAUSADA);

        assertEquals(SyncState.LOCAL, p.getSyncState());
    }
}
