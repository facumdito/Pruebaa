package ar.com.rosario.realestate.ml.vision;

import java.util.List;
import java.util.Map;

/**
 * Result of a computer-vision analysis run on property photos.
 * Produced by PropertyVisionAnalyzer (YOLO11n detector + CLIP embeddings).
 */
public record VisionAnalysis(
    String propiedadId,
    List<DetectedObject> detections,
    List<Float> clipEmbedding,
    double qualityScore,
    String dominantRoom,
    Map<String, Double> roomDistribution
) {
    public record DetectedObject(String label, double confidence, BoundingBox box) {}
    public record BoundingBox(double x, double y, double width, double height) {}

    public boolean hasPool()    { return detections.stream().anyMatch(d -> "pool".equals(d.label()) && d.confidence() > 0.7); }
    public boolean hasGarage()  { return detections.stream().anyMatch(d -> "garage".equals(d.label()) && d.confidence() > 0.6); }
    public boolean isHighQuality() { return qualityScore >= 0.75; }
}
