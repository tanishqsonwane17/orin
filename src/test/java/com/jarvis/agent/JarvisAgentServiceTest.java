package com.jarvis.agent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jarvis.memory.CommandHistoryService;
import com.jarvis.tools.ToolExecutionResult;
import com.jarvis.tools.ToolRegistry;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JarvisAgentServiceTest {

    private final ToolRegistry toolRegistry = mock(ToolRegistry.class);
    private final CommandHistoryService commandHistoryService = mock(CommandHistoryService.class);
    private final JarvisAgentService agentService = new JarvisAgentService(toolRegistry, commandHistoryService);

    @Test
    void routesOpenCodeEditorToVscode() {
        when(toolRegistry.execute("open_application", Map.of("application", "vscode")))
                .thenReturn(ToolExecutionResult.success("Visual Studio Code opened successfully."));

        AgentResponse response = agentService.process("launch my code editor");

        assertThat(response.success()).isTrue();
        assertThat(response.message()).contains("Visual Studio Code");
        verify(toolRegistry).execute("open_application", Map.of("application", "vscode"));
    }

    @Test
    void routesDevelopmentSetupToWorkspaceTool() {
        when(toolRegistry.execute("configure_workspace", Map.of("workspace", "development")))
                .thenReturn(ToolExecutionResult.success("done"));

        AgentResponse response = agentService.process("configure my development setup");

        assertThat(response.success()).isTrue();
        verify(toolRegistry).execute("configure_workspace", Map.of("workspace", "development"));
    }
}
