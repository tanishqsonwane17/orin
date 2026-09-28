package com.jarvis.os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class WindowsApplicationLauncher implements ApplicationLauncher {

    @Override
    public Process launch(Application application) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(application.command());
        if (application.arguments() != null && !application.arguments().isBlank()) {
            command.addAll(Arrays.asList(application.arguments().split("\\s+")));
        }
        return new ProcessBuilder(command).start();
    }
}
