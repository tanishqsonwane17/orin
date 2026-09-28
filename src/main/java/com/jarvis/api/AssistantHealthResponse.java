package com.jarvis.api;

import java.time.Instant;

public record AssistantHealthResponse(
        String status,
        String service,
        Instant timestamp) {
}
