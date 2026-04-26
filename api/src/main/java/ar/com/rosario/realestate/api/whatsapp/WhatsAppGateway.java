package ar.com.rosario.realestate.api.whatsapp;

/**
 * Outbound port for WhatsApp messaging via Meta Cloud API.
 */
public interface WhatsAppGateway {

    void sendText(String to, String body);

    void sendTemplate(String to, String templateName, String languageCode, Object... params);
}
