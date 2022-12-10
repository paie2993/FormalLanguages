package org.compilers.grammar.ui;

import org.compilers.grammar.model.Grammar;
import org.compilers.grammar.model.GrammarReader;
import org.compilers.grammar.model.vocab.non_terminal.NonTerminal;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.StringJoiner;

public final class CommandLineInterface {

    private static final String grammarFile = "src/main/java/org/compilers/grammar/g2.txt";

    private final Grammar grammar;

    public CommandLineInterface() throws IOException {
        this.grammar = GrammarReader.readGrammar(grammarFile);
    }

    private static void printMenu() {
        System.out.println(
                """
                        1. non-terminals
                        2. terminals
                        3. set of productions
                        4. productions for non-terminal
                        5. is context free grammar
                        6. exit
                        """
        );
    }

    public void run() {
        final Scanner scanner = new Scanner(System.in);

        boolean done = false;
        while (!done) {

            printMenu();

            System.out.print("Your option: ");
            final String command = readInput(scanner);

            switch (command) {
                case "1" -> printNonTerminals();
                case "2" -> printTerminals();
                case "3" -> printProductions();
                case "4" -> handleProductionsOfNonTerminal(scanner);
                case "5" -> printIsContextFree();
                case "6" -> done = true;
                default -> System.out.println("Try again");
            }

        }
    }

    private static String readInput(final Scanner scanner) {
        return scanner.nextLine().trim();
    }

    // non-terminals option
    private void printNonTerminals() {
        final var nonTerminals = grammar.nonTerminals();
        final var nonTerminalsString = prepareSetForPrinting(nonTerminals);
        System.out.println(nonTerminalsString);
    }

    // terminals option
    private void printTerminals() {
        final var terminals = grammar.terminals();
        final var terminalsString = prepareSetForPrinting(terminals);
        System.out.println(terminalsString);
    }

    // set of productions option
    private void printProductions() {
        final var productions = grammar.productions();
        productions.forEach(System.out::println); // bound method reference
    }

    // productions of non-terminal option
    private void handleProductionsOfNonTerminal(final Scanner scanner) {
        System.out.print("Non-Terminal: ");
        final var nonTerminalString = readInput(scanner);

        final var nonTerminalOptional = validateNonTerminal(nonTerminalString);

        if (nonTerminalOptional.isEmpty()) {
            return;
        }

        final var nonTerminal = nonTerminalOptional.get();
        printProductionsOfNonTerminal(nonTerminal);
    }

    private Optional<NonTerminal> validateNonTerminal(final String nonTerminalString) {
        final var nonTerminal = new NonTerminal(nonTerminalString);

        if (!grammar.containsNonTerminal(nonTerminal)) {
            System.out.println("The given non-terminal " + nonTerminal + " is not in the grammar");
            return Optional.empty();
        }

        return Optional.of(nonTerminal);
    }


    private void printProductionsOfNonTerminal(final NonTerminal nonTerminal) {
        final var productions = grammar.productions(nonTerminal);
        productions.forEach(System.out::println);
    }

    // is context free grammar option
    private void printIsContextFree() {
        final var isCfg = grammar.isContextFree();
        if (!isCfg) {
            System.out.println("The grammar is not context free");
        }
        System.out.println("The grammar is context free");
    }

    // set printing option
    private static String prepareSetForPrinting(final Set<?> objects) {
        final var joiner = new StringJoiner(", ", "{ ", " }");
        objects.stream().map(Object::toString).forEach(joiner::add); // unbound method reference
        return joiner.toString();
    }
}
















