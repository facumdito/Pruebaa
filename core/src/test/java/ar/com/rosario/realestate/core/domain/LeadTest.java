package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeadTest {

    private Lead unLead() {
        return new Lead(LeadId.newId(), TenantId.of("t-001"), "Juan Pérez", FuenteLead.WHATSAPP);
    }

    @Test
    void constructorEstableceValoresIniciales() {
        Lead l = unLead();
        assertEquals(EstadoLead.NUEVO, l.getEstado());
        assertEquals(0, l.getScore());
        assertEquals(0, l.getBantBudget());
        assertEquals(0, l.getBantAuthority());
        assertEquals(0, l.getBantNeed());
        assertEquals(0, l.getBantTimeline());
        assertFalse(l.isDeleted());
        assertEquals(0L, l.getVersion());
    }

    @Test
    void bantTodosEnCienDaScoreCien() {
        Lead l = unLead();
        l.actualizarBant(100, 100, 100, 100);
        assertEquals(100, l.getScore());
    }

    @Test
    void bantTodosEnCeroMantieneScoreCero() {
        Lead l = unLead();
        l.actualizarBant(0, 0, 0, 0);
        assertEquals(0, l.getScore());
    }

    @Test
    void bantCalculaPonderacionCorrecta() {
        Lead l = unLead();
        // budget=100(30%) + authority=0(15%) + need=100(25%) + timeline=0(30%) = 55
        l.actualizarBant(100, 0, 100, 0);
        assertEquals(55, l.getScore());
    }

    @Test
    void bantPonderacionCompleta() {
        Lead l = unLead();
        // budget=80(30%)=24 + authority=60(15%)=9 + need=70(25%)=17.5 + timeline=50(30%)=15 = 65.5 → 66
        l.actualizarBant(80, 60, 70, 50);
        assertEquals(66, l.getScore());
    }

    @Test
    void bantIncrementaVersionYMarcaLocal() {
        Lead l = unLead();
        l.setSyncState(SyncState.SYNCED);
        l.actualizarBant(50, 50, 50, 50);

        assertEquals(SyncState.LOCAL, l.getSyncState());
        assertEquals(1L, l.getVersion());
    }

    @Test
    void bantBudgetFueraDeRangoLanzaExcepcion() {
        Lead l = unLead();
        assertThrows(IllegalArgumentException.class, () -> l.actualizarBant(101, 50, 50, 50));
        assertThrows(IllegalArgumentException.class, () -> l.actualizarBant(-1, 50, 50, 50));
    }

    @Test
    void bantAuthorityFueraDeRangoLanzaExcepcion() {
        Lead l = unLead();
        assertThrows(IllegalArgumentException.class, () -> l.actualizarBant(50, 101, 50, 50));
    }

    @Test
    void bantNeedFueraDeRangoLanzaExcepcion() {
        Lead l = unLead();
        assertThrows(IllegalArgumentException.class, () -> l.actualizarBant(50, 50, -5, 50));
    }

    @Test
    void bantTimelineFueraDeRangoLanzaExcepcion() {
        Lead l = unLead();
        assertThrows(IllegalArgumentException.class, () -> l.actualizarBant(50, 50, 50, 200));
    }

    @Test
    void actualizarScoreManualDentroDeRango() {
        Lead l = unLead();
        l.actualizarScore(75);
        assertEquals(75, l.getScore());
        assertEquals(1L, l.getVersion());
    }

    @Test
    void actualizarScoreManualFueraDeRangoLanza() {
        Lead l = unLead();
        assertThrows(IllegalArgumentException.class, () -> l.actualizarScore(101));
        assertThrows(IllegalArgumentException.class, () -> l.actualizarScore(-1));
    }

    @Test
    void avanzarEstadoCambiaCorrecto() {
        Lead l = unLead();
        l.avanzarEstado(EstadoLead.CALIFICADO);
        assertEquals(EstadoLead.CALIFICADO, l.getEstado());
        assertEquals(1L, l.getVersion());
    }

    @Test
    void marcarEliminadoSetaFlag() {
        Lead l = unLead();
        l.marcarEliminado();
        assertTrue(l.isDeleted());
        assertEquals(1L, l.getVersion());
    }

    @Test
    void setContactoNoIncrementaVersion() {
        Lead l = unLead();
        l.setTelefono("3413001234");
        l.setEmail("juan@mail.com");
        assertEquals(0L, l.getVersion());
    }
}
