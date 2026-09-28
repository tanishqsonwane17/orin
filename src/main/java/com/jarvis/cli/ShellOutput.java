package com.jarvis.cli;

import com.jarvis.agent.AgentResponse;
import org.springframework.stereotype.Component;

@Component
public class ShellOutput {

    public void printWelcome() {
        println("");
        println("+--------------------------------------+");
        println("|              J A R V I S             |");
        println("|        AI Desktop Assistant          |");
        println("+--------------------------------------+");
        println("");
        println("JARVIS is ready.");
        println("");
    }

    public void prompt() {
        System.out.print("You > ");
    }

    public void agentResponse(AgentResponse response) {
        println("");
        println("JARVIS > " + response.message());
        println("");
    }

    public void println(String value) {
        System.out.println(value);
    }
}
