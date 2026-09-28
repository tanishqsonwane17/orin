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
public class PlayMusicTool implements JarvisTool {

    private final BrowserService browserService;

    public PlayMusicTool(BrowserService browserService) {
        this.browserService = browserService;
    }

    @Override
    public String name() {
        return "play_music";
    }

    @Override
    public String description() {
        return "Open YouTube music search results.";
    }

    @Override
    public PermissionLevel permissionLevel() {
        return PermissionLevel.SAFE;
    }

    @Override
    public ToolExecutionResult execute(Map<String, String> parameters) {
        String query = parameters.getOrDefault("query", "relaxing music").trim();
        String encoded = URLEncoder.encode(query.isBlank() ? "relaxing music" : query, StandardCharsets.UTF_8);
        browserService.openUrl("https://www.youtube.com/results?search_query=" + encoded);
        return ToolExecutionResult.success("Opened music results for " + query + ".");
    }
}
