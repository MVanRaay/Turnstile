package com.zest.turnstile.web;

import com.zest.turnstile.gate.GateController;
import com.zest.turnstile.logging.GateEventLogger;
import jakarta.persistence.PostPersist;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final GateController gate;
    private final GateEventLogger logger;

    public AdminController(GateController gate, GateEventLogger logger) {
        this.gate = gate;
        this.logger = logger;
    }

    @PostMapping("/open")
    public void openGate(@RequestHeader("X-Employee-Id") String employeeId) {
        gate.openGate(500);
        logger.logEmployeeOpen(employeeId);
    }
}
