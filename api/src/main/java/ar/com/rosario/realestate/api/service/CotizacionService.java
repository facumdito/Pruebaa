package ar.com.rosario.realestate.api.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

/**
 * Fetches dólar MEP from dolarapi.com with 15-min Caffeine cache and Resilience4j circuit breaker.
 * Fallback returns last known rate or 1000 ARS/USD if no prior value exists.
 */
@Service
public class CotizacionService {

    private static final Logger log = LoggerFactory.getLogger(CotizacionService.class);
    private static final String DOLAR_API = "https://dolarapi.com/v1/dolares/bolsa";
    private static final String CACHE_KEY = "mep";
    private static final BigDecimal FALLBACK_RATE = BigDecimal.valueOf(1000);

    private final RestClient restClient;
    private final Cache<String, CotizacionMep> cache;

    public CotizacionService() {
        this.restClient = RestClient.builder().baseUrl(DOLAR_API).build();
        this.cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(15))
            .maximumSize(1)
            .build();
    }

    @CircuitBreaker(name = "dolarApi", fallbackMethod = "fallbackMep")
    public CotizacionMep obtenerMep() {
        return cache.get(CACHE_KEY, k -> fetchFromApi());
    }

    @SuppressWarnings("unused")
    public CotizacionMep fallbackMep(Exception ex) {
        log.warn("dolarApi circuit open, using fallback rate: {}", ex.getMessage());
        CotizacionMep cached = cache.getIfPresent(CACHE_KEY);
        return cached != null ? cached : new CotizacionMep(FALLBACK_RATE, LocalDate.now());
    }

    private CotizacionMep fetchFromApi() {
        @SuppressWarnings("unchecked")
        Map<String, Object> body = restClient.get().retrieve().body(Map.class);
        if (body == null) throw new RuntimeException("Empty response from dolarapi");
        double venta = ((Number) body.getOrDefault("venta", 0)).doubleValue();
        return new CotizacionMep(BigDecimal.valueOf(venta), LocalDate.now());
    }

    public record CotizacionMep(BigDecimal tipoCambio, LocalDate fecha) {}
}
