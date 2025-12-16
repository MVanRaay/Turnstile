package com.zest.turnstile.gate;

import com.zest.turnstile.scanner.TurnstileClient;
import org.springframework.stereotype.Component;

@Component
public class GateController {
    private final TurnstileClient client;

    public GateController(TurnstileClient client) {
        this.client = client;
    }

    public void openGate(int durationMs) {
        int units = Math.min(255, Math.max(1, durationMs / 50));
        client.sendPacket((byte)0x2A, new byte[] { 0x01, (byte) units });
    }

    public void deny() {
        client.sendPacket((byte)0x04, new byte[] {0x08, 0x01, 0x10, 0x02});
    }
}
