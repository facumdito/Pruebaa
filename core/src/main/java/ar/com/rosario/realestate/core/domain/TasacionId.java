package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.EntityId;

import java.util.Objects;

public record TasacionId(String value) implements EntityId {

    public TasacionId {
        Objects.requireNonNull(value, "tasacionId must not be null");
    }

    public static TasacionId newId() {
        return new TasacionId(EntityId.newUuid());
    }

    public static TasacionId of(String value) {
        return new TasacionId(value);
    }
}
