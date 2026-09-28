package com.jarvis.os;

public record Application(
        String id,
        String name,
        String displayName,
        String command,
        String arguments,
        String processName,
        boolean enabled
) {
}
