package ar.com.rosario.realestate.shared;

/**
 * ThreadLocal holder for the current tenant identifier.
 * Set by TenantContextFilter (auth module) from the JWT claim,
 * read by TenantIdentifierResolver (persistence module) to tell Hibernate which tenant.
 * Always call clear() in a finally block after request processing.
 */
public final class TenantContext {

    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private TenantContext() {}

    public static void set(String tenantId) {
        CURRENT.set(tenantId);
    }

    public static String get() {
        String t = CURRENT.get();
        return t != null ? t : "system";
    }

    public static boolean hasValue() {
        return CURRENT.get() != null;
    }

    public static void clear() {
        CURRENT.remove();
    }
}
