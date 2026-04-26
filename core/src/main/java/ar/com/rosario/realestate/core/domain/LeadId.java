package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.EntityId;

import java.util.Objects;

public record LeadId(String value) implements EntityId {

    public LeadId {
        Objects.requireNonNull(value, "leadId must not be null");
    }

    public static LeadId newId() {
        return new LeadId(EntityId.newUuid());
    }

    public static LeadId of(String value) {
        return new LeadId(value);
    }
}
