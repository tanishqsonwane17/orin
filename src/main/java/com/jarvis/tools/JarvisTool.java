package com.jarvis.tools;

import com.jarvis.security.PermissionLevel;
import java.util.Map;

public interface JarvisTool {

    String name();

    String description();

    PermissionLevel permissionLevel();

    ToolExecutionResult execute(Map<String, String> parameters);
}
