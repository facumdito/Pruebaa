package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.EntityId;

import java.util.Objects;

public record AgenteId(String value) implements EntityId {

    public AgenteId {
        Objects.requireNonNull(value, "agenteId must not be null");
    }

    public static AgenteId newId() {
        return new AgenteId(EntityId.newUuid());
    }

    public static AgenteId of(String value) {
        return new AgenteId(value);
    }
}
