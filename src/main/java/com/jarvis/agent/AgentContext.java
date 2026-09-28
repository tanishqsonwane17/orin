package com.jarvis.agent;

import java.time.Instant;

public record AgentContext(String command, Instant receivedAt) {

    public static AgentContext forCommand(String command) {
        return new AgentContext(command, Instant.now());
    }
}
