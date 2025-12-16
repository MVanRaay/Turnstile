CREATE TABLE gate_event_log (
    id BIGSERIAL PRIMARY KEY,
    event_id TEXT NOT NULL,
    source VARCHAR(32) NOT NULL,
    action VARCHAR(32) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    barcode_hash TEXT,
    employee_id TEXT,
    reason TEXT
);

CREATE INDEX idx_event_id ON gate_event_log(event_id);
CREATE INDEX idx_timestamp ON gate_event_log(timestamp);