package com.jarvis.api;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {

    public AssistantMessageResponse respondTo(String userMessage) {
        String responseMessage = "Orin received your message: \"" + userMessage
                + "\". AI and desktop actions are not enabled in this MVP.";

        return new AssistantMessageResponse(
                UUID.randomUUID().toString(),
                responseMessage,
                "received",
                Instant.now());
    }

    public AssistantHealthResponse health() {
        return new AssistantHealthResponse("online", "orin-assistant", Instant.now());
    }
}
