package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgenteTest {

    private Agente unAgente() {
        return new Agente(AgenteId.newId(), TenantId.of("t-001"),
                          "María García", "maria@inmobiliaria.com");
    }

    @Test
    void constructorEstableceActivoTrue() {
        Agente a = unAgente();
        assertTrue(a.isActivo());
        assertEquals(0L, a.getVersion());
        assertEquals(SyncState.LOCAL, a.getSyncState());
    }

    @Test
    void desactivarSetaActivoFalse() {
        Agente a = unAgente();
        a.desactivar();
        assertFalse(a.isActivo());
        assertEquals(1L, a.getVersion());
    }

    @Test
    void activarRestableceActivo() {
        Agente a = unAgente();
        a.desactivar();
        a.activar();
        assertTrue(a.isActivo());
        assertEquals(2L, a.getVersion());
    }

    @Test
    void actualizarContactoGuardaValores() {
        Agente a = unAgente();
        a.actualizarContacto("nueva@mail.com", "3413009999");
        assertEquals("nueva@mail.com", a.getEmail());
        assertEquals("3413009999", a.getTelefono());
        assertEquals(1L, a.getVersion());
    }

    @Test
    void modificacionMarcaSyncStateLocal() {
        Agente a = unAgente();
        a.setSyncState(SyncState.SYNCED);
        a.desactivar();
        assertEquals(SyncState.LOCAL, a.getSyncState());
    }
}
