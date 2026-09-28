package com.jarvis.ai;

import java.util.Map;

public interface AIProvider {

    String chooseTool(String command, Map<String, String> availableTools);
}
