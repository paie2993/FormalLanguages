package org.compilers;

import org.compilers.analyser.Analyser;

public class Main {

    public static void main(String[] args) {
        try {
            Analyser.processProgram("src/main/java/files/programs/P3.txt");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
