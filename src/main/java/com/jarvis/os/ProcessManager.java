package com.jarvis.os;

import com.jarvis.tools.ToolExecutionResult;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProcessManager {

    public ToolExecutionResult closeConfigured(Application application) {
        if (application.processName() == null || application.processName().isBlank()) {
            return ToolExecutionResult.failure("I couldn't close " + application.displayName() + " because its process name is not configured.");
        }
        try {
            Process process = new ProcessBuilder(List.of("taskkill", "/IM", application.processName())).start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return ToolExecutionResult.success(application.displayName() + " closed successfully.");
            }
            return ToolExecutionResult.failure("I couldn't close " + application.displayName() + ".");
        } catch (Exception ex) {
            return ToolExecutionResult.failure("I couldn't close " + application.displayName() + ".");
        }
    }
}
