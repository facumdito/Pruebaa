package ar.com.rosario.realestate.api.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * MercadoPago Preapproval (suscripciones recurrentes) via REST API.
 * Phase 17: creates monthly subscriptions for tenant billing.
 * Endpoint: api.mercadopago.com/preapproval
 */
@Component
public class MercadoPagoAdapter {

    private static final Logger log = LoggerFactory.getLogger(MercadoPagoAdapter.class);
    private static final String MP_BASE = "https://api.mercadopago.com";

    private final RestClient restClient;
    private final boolean enabled;

    public MercadoPagoAdapter(@Value("${mercadopago.access-token:}") String accessToken) {
        this.enabled = !accessToken.isBlank();
        this.restClient = RestClient.builder()
            .baseUrl(MP_BASE)
            .defaultHeader("Authorization", "Bearer " + accessToken)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    public String crearSuscripcion(String tenantId, String payerEmail,
                                    String planName, BigDecimal mensualUsd) {
        if (!enabled) {
            log.info("[MP STUB] tenant={} plan={} amount={}", tenantId, planName, mensualUsd);
            return "stub-" + UUID.randomUUID();
        }

        Map<String, Object> autoRecurring = Map.of(
            "frequency",          1,
            "frequency_type",     "months",
            "transaction_amount", mensualUsd,
            "currency_id",        "USD"
        );
        Map<String, Object> body = Map.of(
            "reason",         "Rosario Real Estate — plan " + planName,
            "payer_email",    payerEmail,
            "back_url",       "https://app.rosariorealestate.com.ar/billing/callback",
            "auto_recurring", autoRecurring
        );

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restClient.post()
            .uri("/preapproval")
            .body(body)
            .retrieve()
            .body(Map.class);

        String id = response != null ? String.valueOf(response.getOrDefault("id", "")) : "";
        log.info("MP subscription created id={} tenant={}", id, tenantId);
        return id;
    }
}
