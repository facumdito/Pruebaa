package ar.com.rosario.realestate.core.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * AVM valuation result. Never expose a point estimate alone — always include confidence band.
 */
public record TasacionResult(
    PropiedadId propiedadId,
    BigDecimal valorEstimadoUsd,
    BigDecimal bandaInferiorUsd,
    BigDecimal bandaSuperiorUsd,
    double mdape,
    double pe10,
    double pe20,
    String modelVersion,
    Instant calculadoEn
) {
    public boolean isHighConfidence() {
        return pe10 >= 0.70;
    }
}
