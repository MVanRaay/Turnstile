package com.zest.turnstile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.scanner")
public class ScannerProperties {
    private String host;
    private int port;
    private int heartbeatSeconds;
    private int reconnectSeconds;

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public int getHeartbeatSeconds() { return heartbeatSeconds; }
    public void setHeartbeatSeconds(int heartbeatSeconds) { this.heartbeatSeconds = heartbeatSeconds; }

    public int getReconnectSeconds() { return reconnectSeconds; }
    public void setReconnectSeconds(int reconnectSeconds) { this.reconnectSeconds = reconnectSeconds; }
}
