package ar.com.rosario.realestate.api.dto;

import ar.com.rosario.realestate.core.domain.TasacionResult;

import java.math.BigDecimal;
import java.time.Instant;

public record TasacionDto(
    String propiedadId,
    BigDecimal valorEstimadoUsd,
    BigDecimal bandaInferiorUsd,
    BigDecimal bandaSuperiorUsd,
    double mdape,
    double pe10,
    double pe20,
    String modelVersion,
    boolean highConfidence,
    Instant timestamp
) {
    public static TasacionDto from(String propiedadId, TasacionResult r) {
        return new TasacionDto(
            propiedadId,
            r.valorEstimadoUsd(),
            r.bandaInferiorUsd(),
            r.bandaSuperiorUsd(),
            r.mdape(),
            r.pe10(),
            r.pe20(),
            r.modelVersion(),
            r.isHighConfidence(),
            Instant.now()
        );
    }
}
