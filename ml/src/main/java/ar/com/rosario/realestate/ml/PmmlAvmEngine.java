package ar.com.rosario.realestate.ml;

import ar.com.rosario.realestate.core.domain.Propiedad;
import ar.com.rosario.realestate.core.domain.TasacionResult;
import ar.com.rosario.realestate.core.port.out.AvmEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

/**
 * AVM inference via jpmml-evaluator.
 * PMML model trained in Python (sklearn2pmml) and loaded from classpath or filesystem.
 * Activated in Phase 8 — stub returns UnsupportedOperationException until model is trained.
 */
public class PmmlAvmEngine implements AvmEngine {

    private static final String MODEL_VERSION = "stub-0.0";

    @Override
    public TasacionResult tasar(Propiedad propiedad) {
        // Phase 8: load avm_rosario_vN.pmml, build input map, call evaluator.evaluate(),
        //          inverse log: Math.exp(logPrecio), build 80% confidence band from bootstrap.
        throw new UnsupportedOperationException(
            "AVM model not yet trained — implement in Phase 8 (PmmlAvmEngine)");
    }

    @Override
    public String modelVersion() {
        return MODEL_VERSION;
    }

    private BigDecimal round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
