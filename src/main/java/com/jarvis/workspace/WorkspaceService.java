package com.jarvis.workspace;

import com.jarvis.browser.BrowserService;
import com.jarvis.os.ApplicationLauncher;
import com.jarvis.os.WindowsApplicationService;
import com.jarvis.tools.ToolExecutionResult;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceService.class);

    private final WorkspaceRepository workspaceRepository;
    private final WindowsApplicationService applicationService;
    private final ApplicationLauncher applicationLauncher;
    private final BrowserService browserService;

    public WorkspaceService(
            WorkspaceRepository workspaceRepository,
            WindowsApplicationService applicationService,
            ApplicationLauncher applicationLauncher,
            BrowserService browserService
    ) {
        this.workspaceRepository = workspaceRepository;
        this.applicationService = applicationService;
        this.applicationLauncher = applicationLauncher;
        this.browserService = browserService;
    }

    public ToolExecutionResult configure(String workspaceName) {
        return workspaceRepository.findEnabledByName(workspaceName)
                .map(this::configureWorkspace)
                .orElseGet(() -> ToolExecutionResult.failure("I couldn't configure " + workspaceName + " because it isn't configured."));
    }

    private ToolExecutionResult configureWorkspace(Workspace workspace) {
        log.info("WORKSPACE_STARTED {}", workspace.name());
        List<String> lines = new ArrayList<>();
        int issues = 0;
        lines.add("Configuring " + workspace.displayName() + "...");
        lines.add("");

        for (WorkspaceApplication workspaceApplication : applicationService.findByWorkspace(workspace.id())) {
            try {
                applicationLauncher.launch(workspaceApplication.application());
                lines.add("[OK] " + workspaceApplication.application().displayName());
            } catch (Exception ex) {
                issues++;
                lines.add("[X] " + workspaceApplication.application().displayName() + " - launch failed");
            }
        }

        for (WorkspaceUrl workspaceUrl : workspaceRepository.findUrls(workspace.id())) {
            try {
                browserService.openUrl(workspaceUrl.url());
                lines.add("[OK] " + workspaceUrl.displayName());
            } catch (Exception ex) {
                issues++;
                lines.add("[X] " + workspaceUrl.displayName() + " - browser failed");
            }
        }

        lines.add("");
        if (issues == 0) {
            lines.add(workspace.displayName() + " configured successfully.");
            log.info("WORKSPACE_COMPLETED {}", workspace.name());
            return ToolExecutionResult.success(String.join(System.lineSeparator(), lines));
        }
        lines.add("Setup completed with " + issues + " issue(s).");
        log.info("WORKSPACE_COMPLETED_WITH_ISSUES {} {}", workspace.name(), issues);
        return ToolExecutionResult.failure(String.join(System.lineSeparator(), lines));
    }
}
