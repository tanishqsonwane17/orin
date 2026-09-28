package com.jarvis.cli;

import java.util.Optional;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class TerminalCommandInput implements CommandInput {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Optional<String> readCommand() {
        if (!scanner.hasNextLine()) {
            return Optional.empty();
        }
        return Optional.of(scanner.nextLine().trim());
    }
}