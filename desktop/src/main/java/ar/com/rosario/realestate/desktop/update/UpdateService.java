package ar.com.rosario.realestate.desktop.update;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

/**
 * Phase 20 — Auto-update check.
 *
 * Fetches a version manifest from the update server (HTTPS JSON):
 *   { "version": "0.2.0", "downloadUrl": "https://…/rosario-realestate-0.2.0.msi", "releaseNotes": "…" }
 *
 * If server version > current version, shows a JavaFX Alert offering to open the download page.
 * The actual download/patch is delegated to update4j (org.update4j:update4j:1.5.10) when wired —
 * until then the user is directed to the download URL via the system browser.
 */
public class UpdateService {

    private static final Logger log = LoggerFactory.getLogger(UpdateService.class);
    private static final String CURRENT_VERSION = readCurrentVersion();

    private final String manifestUrl;

    public UpdateService(String manifestUrl) {
        this.manifestUrl = manifestUrl;
    }

    /** Call from a background thread on startup; shows dialog on JavaFX thread if update available. */
    public void checkAsync() {
        if (manifestUrl == null || manifestUrl.isBlank()) {
            log.debug("No update manifest URL configured — skipping update check");
            return;
        }
        Thread.ofVirtual().start(() -> {
            try {
                UpdateManifest manifest = fetchManifest();
                if (manifest != null && isNewer(manifest.version(), CURRENT_VERSION)) {
                    Platform.runLater(() -> promptUpdate(manifest));
                }
            } catch (Exception e) {
                log.warn("Update check failed: {}", e.getMessage());
            }
        });
    }

    private void promptUpdate(UpdateManifest manifest) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
            "Nueva versión disponible: " + manifest.version() + "\n\n" + manifest.releaseNotes(),
            ButtonType.YES, ButtonType.NO);
        alert.setTitle("Actualización disponible");
        alert.setHeaderText("Rosario Real Estate " + manifest.version());
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES && manifest.downloadUrl() != null) {
                openBrowser(manifest.downloadUrl());
            }
        });
    }

    private UpdateManifest fetchManifest() throws IOException {
        URL url = new URL(manifestUrl);
        try (InputStream is = url.openStream()) {
            // Minimal JSON parse (no external dependency)
            String json = new String(is.readAllBytes());
            String version     = extractJsonString(json, "version");
            String downloadUrl = extractJsonString(json, "downloadUrl");
            String notes       = extractJsonString(json, "releaseNotes");
            return new UpdateManifest(version, downloadUrl, notes);
        }
    }

    private static String extractJsonString(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx < 0) return "";
        int start = json.indexOf('"', idx + search.length() + 1) + 1;
        int end   = json.indexOf('"', start);
        return start > 0 && end > start ? json.substring(start, end) : "";
    }

    /** Simple semver comparison — returns true if a > b. */
    static boolean isNewer(String a, String b) {
        int[] va = parseSemver(a), vb = parseSemver(b);
        for (int i = 0; i < 3; i++) {
            if (va[i] != vb[i]) return va[i] > vb[i];
        }
        return false;
    }

    private static int[] parseSemver(String v) {
        String[] parts = v.replaceAll("[^0-9.]", "").split("\\.");
        int[] nums = new int[3];
        for (int i = 0; i < Math.min(parts.length, 3); i++) {
            try { nums[i] = Integer.parseInt(parts[i]); } catch (NumberFormatException ignored) {}
        }
        return nums;
    }

    private static void openBrowser(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (Exception e) {
            log.warn("Could not open browser: {}", e.getMessage());
        }
    }

    private static String readCurrentVersion() {
        try (InputStream is = UpdateService.class.getResourceAsStream("/version.properties")) {
            if (is == null) return "0.1.0";
            Properties p = new Properties();
            p.load(is);
            return p.getProperty("app.version", "0.1.0");
        } catch (Exception e) {
            return "0.1.0";
        }
    }

    public record UpdateManifest(String version, String downloadUrl, String releaseNotes) {}
}
