package com.jarvis.agent;

public record AgentResponse(String message, boolean success) {

    public static AgentResponse success(String message) {
        return new AgentResponse(message, true);
    }

    public static AgentResponse failure(String message) {
        return new AgentResponse(message, false);
    }
}
