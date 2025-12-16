package com.zest.turnstile.web;

import com.zest.turnstile.scanner.TurnstileClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {
    private final TurnstileClient client;

    public HealthController(TurnstileClient client) {
        this.client = client;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", client.isConnected() ? "UP" : "DEGRADED",
                "scannerConnected", client.isConnected()
        );
    }
}
