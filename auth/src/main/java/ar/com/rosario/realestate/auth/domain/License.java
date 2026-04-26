package ar.com.rosario.realestate.auth.domain;

import ar.com.rosario.realestate.shared.TenantId;

import java.time.Instant;

public record License(
    TenantId tenantId,
    String plan,
    Instant expiresAt,
    int maxMachines,
    boolean valid
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public boolean isActive() {
        return valid && !isExpired();
    }
}
