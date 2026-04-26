package ar.com.rosario.realestate.api.google;

import ar.com.rosario.realestate.core.domain.Visita;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * Syncs Visitas to Google Calendar.
 * Creates/updates events with property address, lead name, agente as attendee.
 */
@Component
public class GoogleCalendarAdapter {

    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarAdapter.class);
    private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR_EVENTS);
    private static final String APP_NAME = "RosarioRealEstate";

    private final GoogleCredentialProvider credProvider;
    private final String calendarId;

    public GoogleCalendarAdapter(GoogleCredentialProvider credProvider,
                                  @Value("${google.calendar.id:primary}") String calendarId) {
        this.credProvider = credProvider;
        this.calendarId = calendarId;
    }

    /**
     * Creates or updates a calendar event for a visita.
     * Returns the Google Calendar event ID for future updates.
     */
    public String upsertVisita(Visita visita, String propiedadDireccion,
                                String leadNombre, String agenteEmail) {
        if (calendarId.isBlank() || "primary".equals(calendarId) && isStubMode()) {
            log.info("[Calendar STUB] upsert visita={} propiedad={}", visita.getId().value(), propiedadDireccion);
            return "stub-" + visita.getId().value();
        }
        try {
            Calendar service = buildService();
            Event event = buildEvent(visita, propiedadDireccion, leadNombre, agenteEmail);

            Event created = service.events().insert(calendarId, event).execute();
            log.info("Calendar event created id={} visita={}", created.getId(), visita.getId().value());
            return created.getId();
        } catch (IOException e) {
            throw new RuntimeException("Error creating calendar event: " + e.getMessage(), e);
        }
    }

    public void cancelarVisita(String calendarEventId) {
        if (isStubMode()) {
            log.info("[Calendar STUB] cancel eventId={}", calendarEventId);
            return;
        }
        try {
            buildService().events().delete(calendarId, calendarEventId).execute();
        } catch (IOException e) {
            log.warn("Failed to delete calendar event {}: {}", calendarEventId, e.getMessage());
        }
    }

    private Event buildEvent(Visita visita, String direccion, String leadNombre, String agenteEmail) {
        Instant start = visita.getFechaHora();
        Instant end = start.plusSeconds(visita.getDuracionMinutos() * 60L);

        Event event = new Event()
                .setSummary("Visita: " + direccion)
                .setDescription("Lead: " + leadNombre + "\nVisita ID: " + visita.getId().value());

        event.setStart(new EventDateTime().setDateTime(new DateTime(start.toEpochMilli())));
        event.setEnd(new EventDateTime().setDateTime(new DateTime(end.toEpochMilli())));

        if (agenteEmail != null && !agenteEmail.isBlank()) {
            event.setAttendees(List.of(
                new EventAttendee().setEmail(agenteEmail).setDisplayName("Agente")
            ));
        }
        return event;
    }

    private boolean isStubMode() {
        try {
            credProvider.credentials(SCOPES);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private Calendar buildService() throws IOException {
        return new Calendar.Builder(
                credProvider.transport(),
                GoogleCredentialProvider.JSON_FACTORY,
                credProvider.credentials(SCOPES))
                .setApplicationName(APP_NAME)
                .build();
    }
}
