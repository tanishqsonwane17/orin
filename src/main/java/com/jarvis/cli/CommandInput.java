package com.jarvis.cli;

import java.util.Optional;

public interface CommandInput {

    Optional<String> readCommand();
}