package com.jarvis.tools;

import com.jarvis.security.PermissionService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ToolRegistry {

    private static final Logger log = LoggerFactory.getLogger(ToolRegistry.class);

    private final Map<String, JarvisTool> tools;
    private final PermissionService permissionService;

    public ToolRegistry(List<JarvisTool> tools, PermissionService permissionService) {
        this.tools = tools.stream().collect(Collectors.toUnmodifiableMap(JarvisTool::name, Function.identity()));
        this.permissionService = permissionService;
    }

    public Optional<JarvisTool> find(String name) {
        return Optional.ofNullable(tools.get(name));
    }

    public ToolExecutionResult execute(String name, Map<String, String> parameters) {
        JarvisTool tool = tools.get(name);
        if (tool == null) {
            return ToolExecutionResult.failure("That tool is not available.");
        }
        if (!permissionService.canExecute(tool.permissionLevel())) {
            return ToolExecutionResult.failure("That action requires confirmation and is not enabled yet.");
        }
        try {
            log.info("TOOL_STARTED {}", name);
            ToolExecutionResult result = tool.execute(parameters);
            log.info(result.success() ? "TOOL_COMPLETED {}" : "TOOL_FAILED {}", name);
            return result;
        } catch (Exception ex) {
            log.error("TOOL_FAILED {}", name, ex);
            return ToolExecutionResult.failure("The tool failed, but JARVIS is still running.");
        }
    }
}
