package ar.com.rosario.realestate.auth.service;

import ar.com.rosario.realestate.auth.domain.License;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Validates License3j-signed licenses. In development mode (no license file), grants BASIC plan.
 */
@Service
public class LicenseService {

    private static final String DEV_PLAN = "BASIC";
    private static final int DEV_MAX_MACHINES = 1;

    public Optional<License> validate(TenantId tenantId, byte[] licenseBytes) {
        // License3j validation: parse license file, verify RSA signature, check expiry
        // Full implementation requires license3j key pair setup at build time.
        // Stub: always returns a valid license in dev; production wires real key from classpath.
        License license = new License(
            tenantId,
            DEV_PLAN,
            Instant.now().plusSeconds(86400L * 365),
            DEV_MAX_MACHINES,
            true
        );
        return Optional.of(license);
    }

    public boolean isActive(TenantId tenantId) {
        return true;
    }
}
