package org.compilers.application;

import org.compilers.automaton_io.cmd_ui.CommandLineInterface;
import org.compilers.automaton_io.cmd_ui.UserInterface;

public final class Application {

    public static void main(String[] args) {
        try {
            final UserInterface userInterface = CommandLineInterface.instance();
            userInterface.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
