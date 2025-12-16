package com.zest.turnstile.eventbrite.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventbriteAttendeesResponse {
    private Pagination pagination;
    private List<Attendee> attendees;

    public Pagination getPagination() {
        return pagination;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }
}
