package org.compilers.grammar;

import org.compilers.grammar.ui.CommandLineInterface;

import java.io.IOException;

public final class Application {

    public static void main(final String[] args) throws IOException {
        final var cmd = new CommandLineInterface();
        cmd.run();
    }
}
