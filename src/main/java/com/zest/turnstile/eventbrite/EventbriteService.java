package com.zest.turnstile.eventbrite;

import com.zest.turnstile.config.EventbriteProperties;
import com.zest.turnstile.eventbrite.dto.EventbriteAttendeesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Service
public class EventbriteService {
    private final EventbriteProperties props;
    private final RestClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public EventbriteService(EventbriteProperties props, RestClient.Builder clientBuilder) {
        this.props = props;
        String url = "https://www.eventbriteapi.com/v3/events/" + props.getEventId() + "/attendees/";
        this.client = clientBuilder.baseUrl(url).build();
    }

    public ValidationResult validateTicket(String barcode) {
        try {
            ResponseEntity<String> response = client.get()
                    .uri("?search=" + barcode)
                    .header("Authorization", "Bearer " + props.getApiKey())
                    .retrieve()
                    .toEntity(String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return ValidationResult.invalid("Eventbrite HTTP" + response.getStatusCode());
            }
            EventbriteAttendeesResponse body = mapper.readValue(response.getBody(), EventbriteAttendeesResponse.class);

            if (body.getPagination().getTotal_items() > 0 && body.getAttendees() != null && !body.getAttendees().isEmpty()) {
                String attendeeId = body.getAttendees().getFirst().getId();
                return ValidationResult.valid(attendeeId);
            } else {
                return ValidationResult.invalid("Ticket not found");
            }
        } catch (Exception e) {
            return ValidationResult.invalid("Exception: " + e.getMessage());
        }
    }

    public boolean checkInTicket(String attendeeId) {
        try {
            ResponseEntity<String> response = client.post()
                    .uri(attendeeId + "/checkin/")
                    .header("Authorization", "Bearer " + props.getApiKey())
                    .retrieve()
                    .toEntity(String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String attendeeId;
        private final String reason;

        private ValidationResult(boolean valid, String attendeeId, String reason) {
            this.valid = valid;
            this.attendeeId = attendeeId;
            this.reason = reason;
        }

        public boolean isValid() {
            return valid;
        }

        public String getAttendeeId() {
            return attendeeId;
        }

        public String getReason() {
            return reason;
        }

        public static ValidationResult valid(String attendeeId) {
            return new ValidationResult(true, attendeeId, "");
        }

        public static ValidationResult invalid(String reason) {
            return new ValidationResult(false, "", reason);
        }
    }
}
