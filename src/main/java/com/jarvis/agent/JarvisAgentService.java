package com.jarvis.agent;

import com.jarvis.memory.CommandHistoryService;
import com.jarvis.tools.ToolExecutionResult;
import com.jarvis.tools.ToolRegistry;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JarvisAgentService implements JarvisAgent {

    private static final Logger log = LoggerFactory.getLogger(JarvisAgentService.class);

    private final ToolRegistry toolRegistry;
    private final CommandHistoryService commandHistoryService;

    public JarvisAgentService(ToolRegistry toolRegistry, CommandHistoryService commandHistoryService) {
        this.toolRegistry = toolRegistry;
        this.commandHistoryService = commandHistoryService;
    }

    @Override
    public AgentResponse process(String command) {
        log.info("COMMAND_RECEIVED {}", command);
        try {
            ToolCall toolCall = route(command);
            if (toolCall == null) {
                AgentResponse response = AgentResponse.failure("I don't know how to help with that yet.");
                commandHistoryService.record(command, response.message(), "UNHANDLED");
                return response;
            }

            log.info("TOOL_SELECTED {}", toolCall.name());
            ToolExecutionResult result = toolRegistry.execute(toolCall.name(), toolCall.parameters());
            AgentResponse response = result.success()
                    ? AgentResponse.success(result.message())
                    : AgentResponse.failure(result.message());
            commandHistoryService.record(command, response.message(), result.success() ? "SUCCESS" : "FAILED");
            return response;
        } catch (Exception ex) {
            log.error("AGENT_FAILED", ex);
            AgentResponse response = AgentResponse.failure("Something went wrong, but JARVIS is still running.");
            commandHistoryService.record(command, response.message(), "FAILED");
            return response;
        }
    }

    private ToolCall route(String command) {
        String normalized = command.toLowerCase(Locale.ROOT).trim();
        if (normalized.isBlank()) {
            return null;
        }
        if (normalized.contains("configure") || normalized.contains("setup")) {
            return new ToolCall("configure_workspace", Map.of("workspace", workspaceName(normalized)));
        }
        if (normalized.contains("system info") || normalized.contains("computer info")) {
            return new ToolCall("get_system_info", Map.of());
        }
        if (normalized.startsWith("close ") || normalized.startsWith("quit ")) {
            return new ToolCall("close_application", Map.of("application", lastWords(normalized, "close", "quit")));
        }
        if (normalized.contains("search youtube for")) {
            return new ToolCall("search_web", Map.of("site", "youtube", "query", after(normalized, "search youtube for")));
        }
        if (normalized.contains("play") && (normalized.contains("music") || normalized.contains("song"))) {
            return new ToolCall("play_music", Map.of("query", normalized.replace("play", "").trim()));
        }
        if (normalized.startsWith("search ")) {
            return new ToolCall("search_web", Map.of("query", after(normalized, "search")));
        }
        if (normalized.contains("open youtube")) {
            return new ToolCall("open_url", Map.of("url", "https://www.youtube.com"));
        }
        if (normalized.contains("open github")) {
            return new ToolCall("open_url", Map.of("url", "https://github.com"));
        }
        if (normalized.startsWith("open http") || normalized.startsWith("launch http")) {
            return new ToolCall("open_url", Map.of("url", normalized.substring(normalized.indexOf("http")).trim()));
        }
        if (normalized.startsWith("open ") || normalized.startsWith("launch ") || normalized.contains("code editor")) {
            return new ToolCall("open_application", Map.of("application", applicationName(normalized)));
        }
        return null;
    }

    private String applicationName(String command) {
        if (command.contains("code editor") || command.contains("vs code") || command.contains("visual studio code")) {
            return "vscode";
        }
        return lastWords(command, "open", "launch", "start");
    }

    private String workspaceName(String command) {
        if (command.contains("development") || command.contains("dev")) {
            return "development";
        }
        String cleaned = command.replace("configure", "").replace("my", "").replace("workspace", "").replace("setup", "").trim();
        return cleaned.isBlank() ? "development" : cleaned;
    }

    private String lastWords(String command, String... prefixes) {
        String cleaned = command;
        for (String prefix : prefixes) {
            cleaned = cleaned.replaceFirst("^" + prefix + "\\s+", "");
        }
        return cleaned.trim();
    }

    private String after(String command, String marker) {
        int index = command.indexOf(marker);
        return index < 0 ? command : command.substring(index + marker.length()).trim();
    }

    private record ToolCall(String name, Map<String, String> parameters) {
        private ToolCall(String name, Map<String, String> parameters) {
            this.name = name;
            this.parameters = new HashMap<>(parameters);
        }
    }
}
