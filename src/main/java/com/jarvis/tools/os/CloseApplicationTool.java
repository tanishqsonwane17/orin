package com.jarvis.tools.os;

import com.jarvis.os.ProcessManager;
import com.jarvis.os.WindowsApplicationService;
import com.jarvis.security.PermissionLevel;
import com.jarvis.tools.JarvisTool;
import com.jarvis.tools.ToolExecutionResult;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CloseApplicationTool implements JarvisTool {

    private final WindowsApplicationService applicationService;
    private final ProcessManager processManager;

    public CloseApplicationTool(WindowsApplicationService applicationService, ProcessManager processManager) {
        this.applicationService = applicationService;
        this.processManager = processManager;
    }

    @Override
    public String name() {
        return "close_application";
    }

    @Override
    public String description() {
        return "Close a configured Windows application.";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.CONFIRM;
    }

    @Override
    public ToolExecutionResult execute(Map<String, String> parameters) {
        String applicationName = parameters.getOrDefault("application", "").trim();
        return applicationService.findEnabledByName(applicationName)
                .map(processManager::closeConfigured)
                .orElseGet(() -> ToolExecutionResult.failure("I couldn't close " + applicationName + " because it isn't configured."));
    }
}
