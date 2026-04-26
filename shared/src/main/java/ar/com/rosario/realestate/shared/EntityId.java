package ar.com.rosario.realestate.shared;

import java.util.UUID;

/**
 * Base type for UUID-based entity identifiers.
 * Extend as a record: {@code public record PropiedadId(String value) implements EntityId {}}
 */
public interface EntityId {

    String value();

    static String newUuid() {
        return UUID.randomUUID().toString();
    }
}
