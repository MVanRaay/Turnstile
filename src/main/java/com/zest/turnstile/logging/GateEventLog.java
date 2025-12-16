package com.zest.turnstile.logging;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "gate_event_log",
        indexes = {
                @Index(name = "idx_event_id", columnList = "eventId"),
                @Index(name = "idx_timestamp", columnList = "timestamp")
        }
)
public class GateEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;

    @Enumerated(EnumType.STRING)
    private SourceType source;

    @Enumerated(EnumType.STRING)
    private ActionType action;

    private Instant timestamp;

    private String barcodeHash;

    private String employeeId;

    private String reason;

    // getters/setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public SourceType getSource() {
        return source;
    }

    public void setSource(SourceType source) {
        this.source = source;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getBarcodeHash() {
        return barcodeHash;
    }

    public void setBarcodeHash(String barcodeHash) {
        this.barcodeHash = barcodeHash;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

