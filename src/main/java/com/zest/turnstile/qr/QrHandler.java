package com.zest.turnstile.qr;

import com.zest.turnstile.eventbrite.EventbriteService;
import com.zest.turnstile.gate.GateController;
import com.zest.turnstile.logging.GateEventLogger;
import com.zest.turnstile.metrics.GateMetrics;
import org.springframework.stereotype.Component;

@Component
public class QrHandler {
    private final EventbriteService eventbrite;
    private final GateController gate;
    private final GateEventLogger logger;
    private final GateMetrics metrics;

    public QrHandler(EventbriteService eb, GateController gate, GateEventLogger logger, GateMetrics metrics) {
        this.eventbrite = eb;
        this.gate = gate;
        this.logger = logger;
        this.metrics = metrics;
    }

    public void handleQr(String qr) {
        String barcode = qr.trim();
        var result = eventbrite.validateTicket(barcode);
        if (result.isValid()) {
            gate.openGate(500);
            logger.logQrOpen(qr);
            metrics.qrOpen();
            eventbrite.checkInTicket(result.getAttendeeId());
        } else {
            gate.deny();
            logger.logQrDeny(qr, result.getReason());
            metrics.qrDeny();
        }
    }
}
