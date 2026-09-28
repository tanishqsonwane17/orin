package com.jarvis.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jarvis.ai")
public record AIModelConfig(String provider, String model, String apiKey, String systemPrompt) {
}
