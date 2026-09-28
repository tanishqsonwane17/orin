package com.jarvis.cli;

import com.jarvis.agent.AgentResponse;
import com.jarvis.agent.JarvisAgent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JarvisShell implements CommandLineRunner {

    private final JarvisAgent jarvisAgent;
    private final ShellOutput shellOutput;
    private final CommandInput commandInput;

    public JarvisShell(JarvisAgent jarvisAgent, ShellOutput shellOutput, CommandInput commandInput) {
        this.jarvisAgent = jarvisAgent;
        this.shellOutput = shellOutput;
        this.commandInput = commandInput;
    }

    @Override
    public void run(String... args) {
        shellOutput.printWelcome();
        while (true) {
            shellOutput.prompt();
            String command = commandInput.readCommand().orElse(null);
            if (command == null) {
                return;
            }
            if (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("quit")) {
                shellOutput.println("JARVIS > Goodbye.");
                return;
            }
            AgentResponse response = jarvisAgent.process(command);
            shellOutput.agentResponse(response);
        }
    }
}