package ar.com.rosario.realestate.api.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Loads Google service-account credentials from GOOGLE_SA_JSON env var.
 * Falls back to Application Default Credentials for local dev.
 */
@Component
public class GoogleCredentialProvider {

    static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final String serviceAccountJson;

    public GoogleCredentialProvider(@Value("${google.service-account-json:}") String saJson) {
        this.serviceAccountJson = saJson;
    }

    public com.google.api.client.http.HttpRequestInitializer credentials(List<String> scopes)
            throws IOException {
        GoogleCredentials creds;
        if (!serviceAccountJson.isBlank()) {
            creds = ServiceAccountCredentials
                    .fromStream(new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8)))
                    .createScoped(scopes);
        } else {
            creds = GoogleCredentials.getApplicationDefault().createScoped(scopes);
        }
        return new HttpCredentialsAdapter(creds);
    }

    public HttpTransport transport() {
        try {
            return GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Google HTTP transport", e);
        }
    }
}
