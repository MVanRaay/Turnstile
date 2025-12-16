package com.zest.turnstile.web;

import com.zest.turnstile.gate.GateController;
import com.zest.turnstile.logging.GateEventLogger;
import com.zest.turnstile.metrics.GateMetrics;
import jakarta.persistence.PostPersist;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final GateController gate;
    private final GateEventLogger logger;
    private final GateMetrics metrics;

    public AdminController(GateController gate, GateEventLogger logger, GateMetrics metrics) {
        this.gate = gate;
        this.logger = logger;
        this.metrics = metrics;
    }

    @PostMapping("/open")
    public void openGate(Authentication auth) {
        String employeeId = auth.getName();
        gate.openGate(500);
        logger.logEmployeeOpen(employeeId);
        metrics.employeeOpen();
    }
}
