package com.jarvis.api;

import java.time.Instant;

public record AssistantMessageResponse(
        String id,
        String message,
        String status,
        Instant timestamp) {
}
