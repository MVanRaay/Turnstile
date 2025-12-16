package com.zest.turnstile.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class GateMetrics {
    private final Counter qrOpens;
    private final Counter qrDenies;
    private final Counter employeeOpens;

    public GateMetrics(MeterRegistry registry) {
        qrOpens = registry.counter("gate.qr.open");
        qrDenies = registry.counter("gate.qr.deny");
        employeeOpens = registry.counter("gate.employee.open");
    }

    public void qrOpen() { qrOpens.increment(); }

    public void qrDeny() { qrDenies.increment(); }

    public void employeeOpen() { employeeOpens.increment(); }
}
