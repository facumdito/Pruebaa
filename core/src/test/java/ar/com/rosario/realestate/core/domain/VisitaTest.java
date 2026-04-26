package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.TenantId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class VisitaTest {

    private Visita unaVisita() {
        return new Visita(
            VisitaId.newId(),
            TenantId.of("t-001"),
            PropiedadId.newId(),
            LeadId.newId(),
            AgenteId.newId(),
            Instant.now().plus(1, ChronoUnit.DAYS),
            60
        );
    }

    @Test
    void constructorEstableceAgendada() {
        Visita v = unaVisita();
        assertEquals(EstadoVisita.AGENDADA, v.getEstado());
        assertEquals(0L, v.getVersion());
    }

    @Test
    void duracionCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
            new Visita(VisitaId.newId(), TenantId.of("t"), PropiedadId.newId(),
                       LeadId.newId(), AgenteId.newId(), Instant.now(), 0));
    }

    @Test
    void confirmarDesdeAgendadaOk() {
        Visita v = unaVisita();
        v.confirmar();
        assertEquals(EstadoVisita.CONFIRMADA, v.getEstado());
        assertEquals(1L, v.getVersion());
    }

    @Test
    void confirmarDesdeConfirmadaLanza() {
        Visita v = unaVisita();
        v.confirmar();
        assertThrows(IllegalStateException.class, v::confirmar);
    }

    @Test
    void realizarDesdeConfirmadaOk() {
        Visita v = unaVisita();
        v.confirmar();
        v.realizar("Interesado en el piso 3");
        assertEquals(EstadoVisita.REALIZADA, v.getEstado());
        assertEquals("Interesado en el piso 3", v.getNotas());
    }

    @Test
    void realizarDesdeAgendadaOk() {
        Visita v = unaVisita();
        v.realizar("Sin nota");
        assertEquals(EstadoVisita.REALIZADA, v.getEstado());
    }

    @Test
    void realizarDesdeCanceladaLanza() {
        Visita v = unaVisita();
        v.cancelar("El lead no puede venir");
        assertThrows(IllegalStateException.class, () -> v.realizar("notas"));
    }

    @Test
    void cancelarDesdeAgendadaOk() {
        Visita v = unaVisita();
        v.cancelar("Reagendamos");
        assertEquals(EstadoVisita.CANCELADA, v.getEstado());
        assertEquals("Reagendamos", v.getNotas());
    }

    @Test
    void cancelarDesdeRealizadaLanza() {
        Visita v = unaVisita();
        v.realizar(null);
        assertThrows(IllegalStateException.class, () -> v.cancelar("motivo"));
    }

    @Test
    void marcarNoShowDesdeConfirmadaOk() {
        Visita v = unaVisita();
        v.confirmar();
        v.marcarNoShow();
        assertEquals(EstadoVisita.NO_SHOW, v.getEstado());
    }

    @Test
    void marcarNoShowDesdeAgendadaLanza() {
        Visita v = unaVisita();
        assertThrows(IllegalStateException.class, v::marcarNoShow);
    }

    @Test
    void reprogramarDesdeAgendadaOk() {
        Visita v = unaVisita();
        Instant nueva = Instant.now().plus(3, ChronoUnit.DAYS);
        AgenteId otroAgente = AgenteId.newId();
        v.reprogramar(nueva, otroAgente);

        assertEquals(EstadoVisita.AGENDADA, v.getEstado());
        assertEquals(nueva, v.getFechaHora());
        assertEquals(otroAgente, v.getAgenteId());
        assertEquals(1L, v.getVersion());
    }

    @Test
    void reprogramarDesdeRealizadaLanza() {
        Visita v = unaVisita();
        v.realizar(null);
        assertThrows(IllegalStateException.class,
            () -> v.reprogramar(Instant.now().plus(1, ChronoUnit.DAYS), null));
    }
}
