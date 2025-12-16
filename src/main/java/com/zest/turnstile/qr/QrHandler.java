package com.zest.turnstile.qr;

import com.zest.turnstile.eventbrite.EventbriteService;
import com.zest.turnstile.gate.GateController;
import com.zest.turnstile.logging.GateEventLogger;
import org.springframework.stereotype.Component;

@Component
public class QrHandler {
    private final EventbriteService eventbrite;
    private final GateController gate;
    private final GateEventLogger logger;

    public QrHandler(EventbriteService eb, GateController gate, GateEventLogger logger) {
        this.eventbrite = eb;
        this.gate = gate;
        this.logger = logger;
    }

    public void handleQr(String qr) {
        String barcode = qr.trim();
        var result = eventbrite.validateTicket(barcode);
        if (result.isValid()) {
            gate.openGate(500);
            logger.logQrOpen(qr);
            eventbrite.checkInTicket(result.getAttendeeId());
        } else {
            gate.deny();
            logger.logQrDeny(qr, result.getReason());
        }
    }
}
