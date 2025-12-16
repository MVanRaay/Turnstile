package com.zest.turnstile.logging;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GateEventRepository extends JpaRepository<GateEventLog, Long> {
}
