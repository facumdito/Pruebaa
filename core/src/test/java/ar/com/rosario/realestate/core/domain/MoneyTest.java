package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void usdCreaConMonedaCorrecta() {
        Money m = Money.usd(new BigDecimal("100000"));
        assertEquals(Money.Currency.USD, m.currency());
        assertEquals(new BigDecimal("100000.00"), m.amount());
    }

    @Test
    void arsCreaConMonedaCorrecta() {
        Money m = Money.ars(new BigDecimal("125000000"));
        assertEquals(Money.Currency.ARS, m.currency());
    }

    @Test
    void convertirUsdAArsMedianteTipoCambio() {
        Money usd = Money.usd(new BigDecimal("100000"));
        BigDecimal mep = new BigDecimal("1250.00");
        Money ars = usd.convertTo(Money.Currency.ARS, mep);

        assertEquals(Money.Currency.ARS, ars.currency());
        assertEquals(new BigDecimal("125000000.00"), ars.amount());
    }

    @Test
    void convertirMismaMonedaDevuelveMismoObjeto() {
        Money usd = Money.usd(new BigDecimal("50000"));
        Money resultado = usd.convertTo(Money.Currency.USD, BigDecimal.TEN);
        assertSame(usd, resultado);
    }

    @Test
    void montoNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
            () -> Money.usd(new BigDecimal("-1")));
    }

    @Test
    void montoNuloLanzaExcepcion() {
        assertThrows(NullPointerException.class, () -> Money.usd(null));
    }

    @Test
    void redondeoADosDecimales() {
        Money m = Money.usd(new BigDecimal("99999.999"));
        assertEquals(new BigDecimal("100000.00"), m.amount());
    }
}
