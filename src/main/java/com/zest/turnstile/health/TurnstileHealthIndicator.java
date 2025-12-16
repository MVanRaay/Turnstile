package com.zest.turnstile.health;

import com.zest.turnstile.scanner.TurnstileClient;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TurnstileHealthIndicator implements HealthIndicator {
    private final TurnstileClient client;

    public TurnstileHealthIndicator(TurnstileClient client) {
        this.client = client;
    }

    @Override
    public Health health() {
        if (client.isConnected()) {
            return Health.up()
                    .withDetail("scanner", "connected")
                    .build();
        } else {
            return Health.down()
                    .withDetail("scanner", "discovered")
                    .build();
        }
    }
}
