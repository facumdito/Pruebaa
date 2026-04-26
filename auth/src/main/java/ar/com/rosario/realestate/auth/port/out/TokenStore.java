package ar.com.rosario.realestate.auth.port.out;

import ar.com.rosario.realestate.shared.TenantId;

import java.util.Optional;

/**
 * Hexagonal port for persisting OAuth2 tokens per tenant.
 * Implemented in Phase 3 with encrypted file-based storage (DPAPI/Keychain/libsecret).
 */
public interface TokenStore {

    void saveRefreshToken(TenantId tenantId, String encryptedToken);

    Optional<String> loadRefreshToken(TenantId tenantId);

    void deleteRefreshToken(TenantId tenantId);
}
