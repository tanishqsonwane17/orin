package com.jarvis.tools.os;

import com.jarvis.os.Application;
import com.jarvis.os.ApplicationLauncher;
import com.jarvis.os.WindowsApplicationService;
import com.jarvis.security.PermissionLevel;
import com.jarvis.tools.JarvisTool;
import com.jarvis.tools.ToolExecutionResult;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OpenApplicationTool implements JarvisTool {

    private final WindowsApplicationService applicationService;
    private final ApplicationLauncher applicationLauncher;

    public OpenApplicationTool(WindowsApplicationService applicationService, ApplicationLauncher applicationLauncher) {
        this.applicationService = applicationService;
        this.applicationLauncher = applicationLauncher;
    }

    @Override
    public String name() {
        return "open_application";
    }

    @Override
    public String description() {
        return "Open a configured Windows application.";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.SAFE;
    }

    @Override
    public ToolExecutionResult execute(Map<String, String> parameters) {
        String applicationName = parameters.getOrDefault("application", "").trim();
        if (applicationName.isBlank()) {
            return ToolExecutionResult.failure("I need an application name to open.");
        }
        return applicationService.findEnabledByName(applicationName)
                .map(this::launch)
                .orElseGet(() -> ToolExecutionResult.failure("I couldn't open " + applicationName + " because it isn't configured."));
    }

    private ToolExecutionResult launch(Application application) {
        try {
            applicationLauncher.launch(application);
            return ToolExecutionResult.success(application.displayName() + " opened successfully.");
        } catch (Exception ex) {
            return ToolExecutionResult.failure("I couldn't open " + application.displayName() + " because the launch failed.");
        }
    }
}
