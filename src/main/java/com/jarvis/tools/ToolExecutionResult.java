package com.jarvis.tools;

public record ToolExecutionResult(boolean success, String message) {

    public static ToolExecutionResult success(String message) {
        return new ToolExecutionResult(true, message);
    }

    public static ToolExecutionResult failure(String message) {
        return new ToolExecutionResult(false, message);
    }
}
