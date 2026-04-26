package ar.com.rosario.realestate.ml;

import ar.com.rosario.realestate.core.domain.Propiedad;
import ar.com.rosario.realestate.core.domain.TasacionResult;
import ar.com.rosario.realestate.core.port.out.AvmEngine;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * AVM inference via jpmml-evaluator.
 * Loads avm_rosario_v1.pmml from classpath. Target was trained on log(precio_usd);
 * output is inverse-transformed with Math.exp().
 * 80% band uses ±1.282σ derived from MdAPE; PE10/PE20 come from held-out validation set stored in PMML metadata.
 */
public class PmmlAvmEngine implements AvmEngine {

    private final Evaluator evaluator;
    private final String version;

    public PmmlAvmEngine(String pmmlResourcePath) {
        try (InputStream is = getClass().getResourceAsStream(pmmlResourcePath)) {
            if (is == null) throw new IllegalStateException("PMML model not found at " + pmmlResourcePath);
            this.evaluator = new LoadingModelEvaluatorBuilder().load(is).build();
            this.evaluator.verify();
            String v = evaluator.getSummary();
            this.version = v != null ? v : "v1";
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load PMML model: " + e.getMessage(), e);
        }
    }

    @Override
    public TasacionResult tasar(Propiedad p) {
        Map<String, Object> inputs = buildInputs(p);
        @SuppressWarnings("unchecked")
        Map<String, Object> results = (Map<String, Object>) EvaluatorUtil.decodeAll(evaluator.evaluate(inputs));

        double logPrecio = toDouble(results.getOrDefault("log_precio_usd", 0.0));
        double logSigma  = toDouble(results.getOrDefault("log_sigma",      0.10));
        double mdape     = toDouble(results.getOrDefault("mdape",           0.15));
        double pe10      = toDouble(results.getOrDefault("pe10",            0.60));
        double pe20      = toDouble(results.getOrDefault("pe20",            0.80));

        double valorEst = Math.exp(logPrecio);
        double inferior = Math.exp(logPrecio - 1.282 * logSigma);
        double superior = Math.exp(logPrecio + 1.282 * logSigma);

        return new TasacionResult(
            p.getId(),
            usd(valorEst),
            usd(inferior),
            usd(superior),
            mdape,
            pe10,
            pe20,
            version,
            Instant.now()
        );
    }

    @Override
    public String modelVersion() {
        return version;
    }

    private Map<String, Object> buildInputs(Propiedad p) {
        Map<String, Object> m = new HashMap<>();
        m.put("m2_cubiertos", p.getM2Cubiertos() != null ? p.getM2Cubiertos().doubleValue() : 0.0);
        m.put("m2_totales",   p.getM2Totales()   != null ? p.getM2Totales().doubleValue()   : 0.0);
        m.put("ambientes",    p.getAmbientes());
        m.put("dormitorios",  p.getDormitorios());
        m.put("banos",        p.getBanos());
        m.put("cocheras",     p.getCocheras());
        m.put("antiguedad_anios", p.getAntiguedadAnios());
        m.put("tipo",         p.getTipo() != null ? p.getTipo().name() : "DEPARTAMENTO");
        m.put("barrio",       p.getBarrio() != null ? p.getBarrio().nombre() : "CENTRO");
        if (p.getCoordenadas() != null) {
            m.put("lat", p.getCoordenadas().latitud());
            m.put("lon", p.getCoordenadas().longitud());
        }
        return m;
    }

    private static double toDouble(Object v) {
        return ((Number) v).doubleValue();
    }

    private static BigDecimal usd(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
