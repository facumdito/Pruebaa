package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.EntityId;

import java.util.Objects;

public record VisitaId(String value) implements EntityId {

    public VisitaId {
        Objects.requireNonNull(value, "visitaId must not be null");
    }

    public static VisitaId newId() {
        return new VisitaId(EntityId.newUuid());
    }

    public static VisitaId of(String value) {
        return new VisitaId(value);
    }
}
