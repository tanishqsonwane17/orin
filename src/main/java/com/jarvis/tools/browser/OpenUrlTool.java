package com.jarvis.tools.browser;

import com.jarvis.browser.BrowserService;
import com.jarvis.security.PermissionLevel;
import com.jarvis.tools.JarvisTool;
import com.jarvis.tools.ToolExecutionResult;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OpenUrlTool implements JarvisTool {

    private final BrowserService browserService;

    public OpenUrlTool(BrowserService browserService) {
        this.browserService = browserService;
    }

    @Override
    public String name() {
        return "open_url";
    }

    @Override
    public String description() {
        return "Open a URL in a browser.";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.SAFE;
    }

    @Override
    public ToolExecutionResult execute(Map<String, String> parameters) {
        String url = parameters.getOrDefault("url", "").trim();
        if (url.isBlank()) {
            return ToolExecutionResult.failure("I need a URL to open.");
        }
        browserService.openUrl(url);
        return ToolExecutionResult.success("Opened " + url + ".");
    }
}
