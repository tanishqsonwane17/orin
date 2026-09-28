package com.jarvis.tools.os;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jarvis.os.Application;
import com.jarvis.os.ApplicationLauncher;
import com.jarvis.os.WindowsApplicationService;
import com.jarvis.tools.ToolExecutionResult;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class OpenApplicationToolTest {

    private final WindowsApplicationService applicationService = mock(WindowsApplicationService.class);
    private final ApplicationLauncher applicationLauncher = mock(ApplicationLauncher.class);
    private final OpenApplicationTool tool = new OpenApplicationTool(applicationService, applicationLauncher);

    @Test
    void launchesConfiguredApplication() throws Exception {
        Application application = new Application("vscode", "vscode", "Visual Studio Code", "code", "", "Code.exe", true);
        when(applicationService.findEnabledByName("vscode")).thenReturn(Optional.of(application));

        ToolExecutionResult result = tool.execute(Map.of("application", "vscode"));

        assertThat(result.success()).isTrue();
        assertThat(result.message()).contains("Visual Studio Code opened successfully");
        verify(applicationLauncher).launch(application);
    }

    @Test
    void reportsMissingConfiguration() {
        when(applicationService.findEnabledByName("docker")).thenReturn(Optional.empty());

        ToolExecutionResult result = tool.execute(Map.of("application", "docker"));

        assertThat(result.success()).isFalse();
        assertThat(result.message()).contains("isn't configured");
    }
}
