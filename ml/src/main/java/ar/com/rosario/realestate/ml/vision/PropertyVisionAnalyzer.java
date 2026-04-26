package ar.com.rosario.realestate.ml.vision;

import java.util.List;
import java.util.Map;

/**
 * Phase 18: Computer-vision pipeline for property photos.
 *
 * Architecture:
 *   1. YOLO11n (ONNX) — object detection: detects pools, garages, kitchens, bathrooms, views
 *   2. OpenCLIP (ViT-B/32) — 512-dim embedding for visual similarity search
 *   3. Quality score: sharpness + brightness + composition heuristics
 *
 * Runtime: YOLO11n and CLIP models run as Python FastAPI microservices (vision-service/).
 * Java side calls REST endpoints with base64-encoded image bytes.
 *
 * Stub returns plausible zero-confidence output so the rest of the system compiles.
 */
public class PropertyVisionAnalyzer {

    private final String visionServiceUrl;

    public PropertyVisionAnalyzer(String visionServiceUrl) {
        this.visionServiceUrl = visionServiceUrl;
    }

    /**
     * Analyzes a property photo given as raw bytes (JPEG/PNG).
     * In stub mode (no vision service configured) returns empty analysis.
     */
    public VisionAnalysis analyze(String propiedadId, byte[] imageBytes) {
        if (visionServiceUrl == null || visionServiceUrl.isBlank()) {
            return stub(propiedadId);
        }
        // Phase 18 full implementation:
        // 1. POST visionServiceUrl/detect   → {detections: [{label, confidence, box}]}
        // 2. POST visionServiceUrl/embed    → {embedding: [float × 512]}
        // 3. POST visionServiceUrl/quality  → {score: float, dominant_room: str}
        // Combine results into VisionAnalysis record.
        return stub(propiedadId);
    }

    /**
     * Bulk-analyze multiple photos; returns the highest-quality analysis.
     */
    public VisionAnalysis bestPhoto(String propiedadId, List<byte[]> photos) {
        if (photos == null || photos.isEmpty()) return stub(propiedadId);
        return photos.stream()
            .map(img -> analyze(propiedadId, img))
            .max(java.util.Comparator.comparingDouble(VisionAnalysis::qualityScore))
            .orElse(stub(propiedadId));
    }

    private VisionAnalysis stub(String propiedadId) {
        return new VisionAnalysis(
            propiedadId,
            List.of(),
            List.of(),
            0.0,
            "unknown",
            Map.of()
        );
    }
}
