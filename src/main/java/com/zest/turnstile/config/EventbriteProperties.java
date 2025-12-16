package com.zest.turnstile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.eventbrite")
public class EventbriteProperties {
    private String apiKey;
    private String eventId;

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
}
