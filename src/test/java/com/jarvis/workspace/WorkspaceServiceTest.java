package com.jarvis.workspace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jarvis.browser.BrowserService;
import com.jarvis.os.Application;
import com.jarvis.os.ApplicationLauncher;
import com.jarvis.os.WindowsApplicationService;
import com.jarvis.tools.ToolExecutionResult;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class WorkspaceServiceTest {

    private final WorkspaceRepository workspaceRepository = mock(WorkspaceRepository.class);
    private final WindowsApplicationService applicationService = mock(WindowsApplicationService.class);
    private final ApplicationLauncher applicationLauncher = mock(ApplicationLauncher.class);
    private final BrowserService browserService = mock(BrowserService.class);
    private final WorkspaceService workspaceService = new WorkspaceService(
            workspaceRepository,
            applicationService,
            applicationLauncher,
            browserService
    );

    @Test
    void configuresWorkspaceApplicationsAndUrls() throws Exception {
        Workspace workspace = new Workspace("development", "development", "Development Setup", "", true);
        Application vscode = new Application("vscode", "vscode", "Visual Studio Code", "code", "", "Code.exe", true);
        when(workspaceRepository.findEnabledByName("development")).thenReturn(Optional.of(workspace));
        when(applicationService.findByWorkspace("development")).thenReturn(List.of(new WorkspaceApplication(vscode)));
        when(workspaceRepository.findUrls("development")).thenReturn(List.of(new WorkspaceUrl("https://github.com", "GitHub")));

        ToolExecutionResult result = workspaceService.configure("development");

        assertThat(result.success()).isTrue();
        assertThat(result.message()).contains("Development Setup configured successfully");
        verify(applicationLauncher).launch(vscode);
        verify(browserService).openUrl("https://github.com");
    }
}
