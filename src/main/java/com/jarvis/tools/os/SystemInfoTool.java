package com.jarvis.tools.os;

import com.jarvis.security.PermissionLevel;
import com.jarvis.tools.JarvisTool;
import com.jarvis.tools.ToolExecutionResult;
import java.lang.management.ManagementFactory;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SystemInfoTool implements JarvisTool {

    @Override
    public String name() {
        return "get_system_info";
    }

    @Override
    public String description() {
        return "Return basic local system information.";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.SAFE;
    }

    @Override
    public ToolExecutionResult execute(Map<String, String> parameters) {
        String message = "OS: " + System.getProperty("os.name")
                + ", Java: " + System.getProperty("java.version")
                + ", processors: " + ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        return ToolExecutionResult.success(message);
    }
}
