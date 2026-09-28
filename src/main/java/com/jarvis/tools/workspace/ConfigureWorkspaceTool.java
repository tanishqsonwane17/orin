package com.jarvis.tools.workspace;

import com.jarvis.security.PermissionLevel;
import com.jarvis.tools.JarvisTool;
import com.jarvis.tools.ToolExecutionResult;
import com.jarvis.workspace.WorkspaceService;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ConfigureWorkspaceTool implements JarvisTool {

    private final WorkspaceService workspaceService;

    public ConfigureWorkspaceTool(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @Override
    public String name() {
        return "configure_workspace";
    }

    @Override
    public String description() {
        return "Open the applications and URLs for a configured workspace.";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.SAFE;
    }

    @Override
    public ToolExecutionResult execute(Map<String, String> parameters) {
        String workspace = parameters.getOrDefault("workspace", "development").trim();
        return workspaceService.configure(workspace.isBlank() ? "development" : workspace);
    }
}
