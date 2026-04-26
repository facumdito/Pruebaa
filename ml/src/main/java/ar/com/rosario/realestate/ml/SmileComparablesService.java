package ar.com.rosario.realestate.ml;

import ar.com.rosario.realestate.core.domain.Propiedad;
import ar.com.rosario.realestate.core.domain.TasacionResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * k-NN comparable sales service using Gower distance (handles mixed numeric/categorical).
 * Phase 11: uses Smile KNNSearch with a custom Gower metric over the active listing corpus.
 */
public class SmileComparablesService {

    private final int k;

    public SmileComparablesService(int k) {
        this.k = k;
    }

    /**
     * Returns the k most similar properties to the subject, ranked by Gower distance.
     * Gower distance normalises numeric features to [0,1] and uses simple match for categoricals.
     */
    public List<Comparable> findComparables(Propiedad subject, List<Propiedad> corpus) {
        if (corpus.isEmpty()) return List.of();

        double[] subjectVec = featureVector(subject);
        double[] ranges = computeRanges(corpus);

        return corpus.stream()
            .map(p -> new Comparable(p, gowerDistance(subjectVec, featureVector(p), ranges, subject, p)))
            .sorted(Comparator.comparingDouble(Comparable::distance))
            .limit(k)
            .toList();
    }

    public TasacionResult tasarPorComparables(Propiedad subject, List<Propiedad> corpus) {
        List<Comparable> comps = findComparables(subject, corpus);
        if (comps.isEmpty()) {
            throw new IllegalStateException("No hay comparables disponibles para tasar por este método");
        }
        double totalWeight = 0;
        double weightedPrice = 0;
        for (Comparable c : comps) {
            if (c.propiedad().getPrecioUsd() == null) continue;
            double weight = 1.0 / (c.distance() + 1e-9);
            weightedPrice += c.propiedad().getPrecioUsd().doubleValue() * weight;
            totalWeight += weight;
        }
        double est = totalWeight > 0 ? weightedPrice / totalWeight : 0;
        double band = est * 0.10;

        return new TasacionResult(
            subject.getId(),
            usd(est),
            usd(est - band),
            usd(est + band),
            0.15,
            0.60,
            0.80,
            "comparable-knn-k" + k,
            Instant.now()
        );
    }

    private double[] featureVector(Propiedad p) {
        return new double[]{
            p.getM2Cubiertos() != null ? p.getM2Cubiertos().doubleValue() : 0,
            p.getM2Totales()   != null ? p.getM2Totales().doubleValue()   : 0,
            p.getAmbientes(),
            p.getDormitorios(),
            p.getBanos(),
            p.getCocheras(),
            p.getAntiguedadAnios()
        };
    }

    private double[] computeRanges(List<Propiedad> corpus) {
        double[] min = {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
        double[] max = new double[7];
        for (Propiedad p : corpus) {
            double[] v = featureVector(p);
            for (int i = 0; i < 7; i++) {
                min[i] = Math.min(min[i], v[i]);
                max[i] = Math.max(max[i], v[i]);
            }
        }
        double[] ranges = new double[7];
        for (int i = 0; i < 7; i++) {
            ranges[i] = max[i] - min[i];
            if (ranges[i] == 0) ranges[i] = 1;
        }
        return ranges;
    }

    private double gowerDistance(double[] a, double[] b, double[] ranges,
                                  Propiedad pa, Propiedad pb) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.abs(a[i] - b[i]) / ranges[i];
        }
        // Categorical: tipo and barrio
        if (pa.getTipo() != null && !pa.getTipo().equals(pb.getTipo())) sum += 1;
        if (pa.getBarrio() != null && pb.getBarrio() != null
                && !pa.getBarrio().nombre().equals(pb.getBarrio().nombre())) sum += 0.5;  // Gower categorical

        return sum / (a.length + 1.5);
    }

    private static BigDecimal usd(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP);
    }

    public record Comparable(Propiedad propiedad, double distance) {}
}
