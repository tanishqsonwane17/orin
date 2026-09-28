package com.jarvis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jarvis")
public record ApplicationConfiguration() {
}
