package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.EntityId;

import java.util.Objects;

public record PropiedadId(String value) implements EntityId {

    public PropiedadId {
        Objects.requireNonNull(value, "propiedadId must not be null");
    }

    public static PropiedadId newId() {
        return new PropiedadId(EntityId.newUuid());
    }

    public static PropiedadId of(String value) {
        return new PropiedadId(value);
    }
}
