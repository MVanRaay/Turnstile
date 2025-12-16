package com.zest.turnstile.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        ScannerProperties.class,
        EventbriteProperties.class
})
public class AppConfig {
}
