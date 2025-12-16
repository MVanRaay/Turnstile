package com.zest.turnstile.eventbrite.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attendee {
    private String id;

    public String getId() {
        return id;
    }
}
