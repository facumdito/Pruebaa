package ar.com.rosario.realestate.api.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/webhooks/mercadopago")
public class MercadoPagoWebhookController {

    private static final Logger log = LoggerFactory.getLogger(MercadoPagoWebhookController.class);

    @PostMapping
    public ResponseEntity<Void> handle(@RequestBody Map<String, Object> payload,
                                        @RequestHeader(value = "x-signature", required = false) String sig) {
        String type = String.valueOf(payload.getOrDefault("type", ""));
        log.info("MP webhook type={}", type);

        switch (type) {
            case "subscription_preapproval" -> handleSubscription(payload);
            case "payment" -> handlePayment(payload);
            default -> log.debug("Unhandled MP event type: {}", type);
        }
        return ResponseEntity.ok().build();
    }

    private void handleSubscription(Map<String, Object> payload) {
        // Phase 17: update tenant plan/active status based on preapproval status change
        log.debug("Subscription event: {}", payload);
    }

    private void handlePayment(Map<String, Object> payload) {
        // Phase 17: record payment, extend license expiry
        log.debug("Payment event: {}", payload);
    }
}
