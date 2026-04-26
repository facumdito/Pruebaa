package ar.com.rosario.realestate.shared;

import java.util.Objects;
import java.util.UUID;

public record TenantId(String value) {

    public TenantId {
        Objects.requireNonNull(value, "tenantId must not be null");
        if (value.isBlank()) throw new IllegalArgumentException("tenantId must not be blank");
    }

    public static TenantId of(String value) {
        return new TenantId(value);
    }

    public static TenantId newId() {
        return new TenantId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
