package org.compilers.automaton_io.cmd_ui;

import org.compilers.automaton_io.automaton_reader.DeterministicFiniteAutomatonReaderImpl;
import org.compilers.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.Term;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

public final class CommandLineInterface implements UserInterface {

    private final FiniteAutomaton finiteAutomaton;
    private boolean running = false;

    // singleton and instantiation
    private CommandLineInterface() throws IOException {
        this.finiteAutomaton = DeterministicFiniteAutomatonReaderImpl.instance().read(Configuration.FINITE_AUTOMATON_DEFINITION_FILE);
    }

    private static final class LazyHolder {
        private static final CommandLineInterface instance = instantiate();

        private static CommandLineInterface instantiate() {
            try {
                return new CommandLineInterface();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static CommandLineInterface instance() {
        return LazyHolder.instance;
    }

    // effectively, the implementation
    @Override
    public void run() {
        running = true;

        try (final Scanner scanner = new Scanner(System.in)) {
            while (running) {
                runIteration(scanner);
            }
        }
    }

    private void runIteration(final Scanner scanner) {
        displayMenu();
        final String command = readCommand(scanner);
        handleCommand(command);
    }

    private static void displayMenu() {
        System.out.println("1  States");
        System.out.println("2  Alphabet");
        System.out.println("3. Transitions");
        System.out.println("4. Initial state");
        System.out.println("5. Final states");
        System.out.println("x. Exit");
    }

    private static String readCommand(final Scanner scanner) {
        return scanner.nextLine().trim();
    }

    private void handleCommand(final String command) {
        switch (command) {
            case "1" -> printTermsOneLine(finiteAutomaton.states());
            case "2" -> printTermsOneLine(finiteAutomaton.alphabet());
            case "3" -> printTermsMultipleLines(finiteAutomaton.transitions());
            case "4" -> printTerm(finiteAutomaton.initialState());
            case "5" -> printTermsOneLine(finiteAutomaton.finalStates());
            case "x" -> handleExit();
            default -> handleMismatch(command);
        }
    }

    private static <T extends Term> void printTermsMultipleLines(final Collection<T> terms) {
        for (final Term term : terms) {
            printTerm(term);
        }
    }

    private static <T extends Term> void printTermsOneLine(final Collection<T> terms) {
        System.out.print("{");
        for (final Term term : terms) {
            System.out.print(" ");
            System.out.print(term.representation());
        }
        System.out.println(" }");
    }

    private static void printTerm(final Term term) {
        System.out.println(term.representation());
    }

    private void handleExit() {
        running = false;
    }

    private static void handleMismatch(final String command) {
        System.out.println("Command not found: " + command);
    }
}
