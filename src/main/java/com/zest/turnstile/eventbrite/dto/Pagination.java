package com.zest.turnstile.eventbrite.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagination {
    private int total_items;

    public int getTotal_items() {
        return total_items;
    }
}
