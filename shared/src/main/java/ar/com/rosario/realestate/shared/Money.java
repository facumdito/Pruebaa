package ar.com.rosario.realestate.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {

    public enum Currency { USD, ARS }

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("amount must be >= 0");
    }

    public static Money usd(BigDecimal amount) {
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), Currency.USD);
    }

    public static Money ars(BigDecimal amount) {
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), Currency.ARS);
    }

    public Money convertTo(Currency target, BigDecimal exchangeRate) {
        if (this.currency == target) return this;
        return new Money(amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP), target);
    }
}
