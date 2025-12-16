package com.zest.turnstile.logging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventContext {

    @Value("${EVENTBRITE_EVENT_ID}")
    private String eventId;

    public String getEventId() {
        return eventId;
    }
}
