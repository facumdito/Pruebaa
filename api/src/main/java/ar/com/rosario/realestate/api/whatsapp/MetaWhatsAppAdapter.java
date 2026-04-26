package ar.com.rosario.realestate.api.whatsapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Meta WhatsApp Cloud API adapter.
 * Endpoint: graph.facebook.com/v19.0/{phoneNumberId}/messages
 * Phase 10: text and template messages; webhook verification handled by WhatsAppWebhookController.
 */
@Component
public class MetaWhatsAppAdapter implements WhatsAppGateway {

    private static final Logger log = LoggerFactory.getLogger(MetaWhatsAppAdapter.class);

    private final RestClient restClient;
    private final String phoneNumberId;

    public MetaWhatsAppAdapter(
            @Value("${whatsapp.token:}") String accessToken,
            @Value("${whatsapp.phone-number-id:}") String phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
        this.restClient = RestClient.builder()
            .baseUrl("https://graph.facebook.com/v19.0")
            .defaultHeader("Authorization", "Bearer " + accessToken)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    @Override
    public void sendText(String to, String body) {
        if (phoneNumberId.isBlank()) {
            log.info("[WhatsApp STUB] to={} body={}", to, body);
            return;
        }
        Map<String, Object> payload = Map.of(
            "messaging_product", "whatsapp",
            "to", normalize(to),
            "type", "text",
            "text", Map.of("body", body)
        );
        post(payload);
    }

    @Override
    public void sendTemplate(String to, String templateName, String languageCode, Object... params) {
        if (phoneNumberId.isBlank()) {
            log.info("[WhatsApp STUB] template={} to={}", templateName, to);
            return;
        }
        List<Map<String, Object>> components = params.length == 0 ? List.of() : List.of(
            Map.of("type", "body", "parameters", buildParams(params))
        );
        Map<String, Object> payload = Map.of(
            "messaging_product", "whatsapp",
            "to", normalize(to),
            "type", "template",
            "template", Map.of(
                "name", templateName,
                "language", Map.of("code", languageCode),
                "components", components
            )
        );
        post(payload);
    }

    private void post(Map<String, Object> payload) {
        restClient.post()
            .uri("/" + phoneNumberId + "/messages")
            .body(payload)
            .retrieve()
            .toBodilessEntity();
    }

    private List<Map<String, Object>> buildParams(Object[] params) {
        return java.util.Arrays.stream(params)
            .map(p -> Map.<String, Object>of("type", "text", "text", String.valueOf(p)))
            .toList();
    }

    private String normalize(String phone) {
        return phone.replaceAll("[^\\d+]", "");
    }
}
