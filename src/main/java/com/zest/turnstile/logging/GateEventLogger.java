package com.zest.turnstile.logging;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class GateEventLogger {
    private final GateEventRepository repo;
    private final EventContext eventContext;

    public GateEventLogger(GateEventRepository repo, EventContext eventContext) {
        this.repo = repo;
        this.eventContext = eventContext;
    }

    public void logQrOpen(String barcode) {
        save(SourceType.EVENTBRITE_QR, ActionType.OPEN, hash(barcode), null, null);
    }

    public void logQrDeny(String barcode, String reason) {
        save(SourceType.EVENTBRITE_QR, ActionType.DENY, hash(barcode), null, reason);
    }

    public void logEmployeeOpen(String employeeId) {
        save(SourceType.EMPLOYEE_REMOTE, ActionType.OPEN, null, employeeId, null);
    }

    private void save(SourceType source, ActionType action, String barcodeHash, String employeeId, String reason) {
        GateEventLog log = new GateEventLog();
        log.setEventId(eventContext.getEventId());
        log.setSource(source);
        log.setAction(action);
        log.setTimestamp(Instant.now());
        log.setBarcodeHash(barcodeHash);
        log.setEmployeeId(employeeId);
        log.setReason(reason);

        repo.save(log);
    }

    private String hash(String barcode) {
        return barcode == null ? null : Integer.toHexString(barcode.hashCode()); // TODO: Replace w/ SHA-256
    }
}
