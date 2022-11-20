package org.compilers.finite_automaton.cmd_ui;

import org.compilers.configuration.Configuration;
import org.compilers.finite_automaton.automaton_io.automaton_reader.FiniteAutomatonReaderImpl;
import org.compilers.finite_automaton.executor.FiniteAutomatonExecutorImpl;
import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.finite_automaton.domain.Term;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Scanner;

public final class CommandLineInterface implements UserInterface {

    private final FiniteAutomaton finiteAutomaton;
    private boolean running = false;

    // singleton and instantiation
    private CommandLineInterface() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.finiteAutomaton = FiniteAutomatonReaderImpl.instance().read(
                Configuration.FINITE_AUTOMATON_DEFINITION_FILE,
                Configuration.FINITE_AUTOMATON_CLASS
        );
    }

    private static final class LazyHolder {
        private static final CommandLineInterface instance = instantiate();

        private static CommandLineInterface instantiate() {
            try {
                return new CommandLineInterface();
            } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static CommandLineInterface instance() {
        return LazyHolder.instance;
    }
    // ... ends here

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
        handleCommand(command, scanner);
    }

    private static void displayMenu() {
        System.out.println("1  States");
        System.out.println("2  Alphabet");
        System.out.println("3. Transitions");
        System.out.println("4. Initial state");
        System.out.println("5. Final states");
        System.out.println("6. Verify if sequence is accepted");
        System.out.println("x. Exit");
    }

    private static String readCommand(final Scanner scanner) {
        return scanner.nextLine().trim();
    }

    private void handleCommand(final String command, final Scanner scanner) {
        switch (command) {
            case "1" -> printTermsOneLine(finiteAutomaton.states());
            case "2" -> printTermsOneLine(finiteAutomaton.alphabet());
            case "3" -> printTermsMultipleLines(finiteAutomaton.transitions());
            case "4" -> printTerm(finiteAutomaton.initialState());
            case "5" -> printTermsOneLine(finiteAutomaton.finalStates());
            case "6" -> handleVerifySequenceAttempt(scanner);
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

    private void handleVerifySequenceAttempt(final Scanner scanner) {
        final String sequence = scanner.nextLine();
        final boolean response = FiniteAutomatonExecutorImpl.instance().apply(finiteAutomaton, sequence);
        printMatchResponse(response);
    }

    private static void printMatchResponse(final boolean matches) {
        if (matches) {
            System.out.println("Match");
        } else {
            System.out.println("Not match");
        }
    }

    private void handleExit() {
        running = false;
    }

    private static void handleMismatch(final String command) {
        System.out.println("Command not found: " + command);
    }
}
