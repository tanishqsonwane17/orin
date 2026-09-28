package com.jarvis.tools.browser;

import com.jarvis.browser.BrowserService;
import com.jarvis.security.PermissionLevel;
import com.jarvis.tools.JarvisTool;
import com.jarvis.tools.ToolExecutionResult;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SearchWebTool implements JarvisTool {

    private final BrowserService browserService;

    public SearchWebTool(BrowserService browserService) {
        this.browserService = browserService;
    }

    @Override
    public String name() {
        return "search_web";
    }

    @Override
    public String description() {
        return "Open web search results.";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.SAFE;
    }

    @Override
    public ToolExecutionResult execute(Map<String, String> parameters) {
        String query = parameters.getOrDefault("query", "").trim();
        if (query.isBlank()) {
            return ToolExecutionResult.failure("I need a search query.");
        }
        if ("youtube".equalsIgnoreCase(parameters.getOrDefault("site", ""))) {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            browserService.openUrl("https://www.youtube.com/results?search_query=" + encoded);
            return ToolExecutionResult.success("Opened YouTube search results for " + query + ".");
        }
        browserService.search(query);
        return ToolExecutionResult.success("Opened search results for " + query + ".");
    }
}
