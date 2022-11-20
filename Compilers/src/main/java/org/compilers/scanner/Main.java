package org.compilers.scanner;

import org.compilers.configuration.Configuration;
import org.compilers.scanner.analyser.Analyser;

public class Main {

    public static void main(String[] args) {
        try {
            Analyser.processProgram(Configuration.PROGRAM_INPUT_FILE);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
