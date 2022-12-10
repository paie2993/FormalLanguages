package org.compilers.grammar.model;

import org.compilers.grammar.model.vocab.non_terminal.NonTerminal;
import org.compilers.grammar.model.production.Production;
import org.compilers.grammar.model.production.ProductionBuilder;
import org.compilers.grammar.model.vocab.terminal.Terminal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class GrammarReader {

    private static final String SEPARATOR = ";";


    public static Grammar readGrammar(final String fileName) throws IOException {
        try (final BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            final var nonTerminals = readNonTerminals(reader);
            final var terminals = readTerminals(reader);

            final var productionBuilder = new ProductionBuilder(nonTerminals, terminals);
            final var productions = readProductions(reader, productionBuilder);

            final var startingNonTerminal = readStartingNonTerminal(reader);

            return new Grammar(nonTerminals, terminals, productions, startingNonTerminal);
        }
    }


    // reading and building of grammar elements
    private static Set<Terminal> readTerminals(final BufferedReader reader) throws IOException {
        final String[] terminals = readTokens(reader);
        return buildElements(terminals, Terminal::new);
    }

    private static Set<NonTerminal> readNonTerminals(final BufferedReader reader) throws IOException {
        final String[] nonTerminals = readTokens(reader);
        return buildElements(nonTerminals, NonTerminal::new);
    }

    private static Set<Production> readProductions(
            final BufferedReader reader,
            final ProductionBuilder productionBuilder
    ) throws IOException {
        final String[] productions = readTokens(reader);
        return buildElements(productions, productionBuilder::buildProduction);
    }

    private static NonTerminal readStartingNonTerminal(final BufferedReader reader) throws IOException {
        final String nonTerminalString = readLine(reader);
        return new NonTerminal(nonTerminalString);
    }


    /**
     * Applies the builder to each token from [tokens]
     */
    private static <T> Set<T> buildElements(final String[] tokens, final Function<String, T> builder) {
        return Arrays.stream(tokens).map(builder).collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Reads a set of tokens from the buffer
     */
    private static String[] readTokens(final BufferedReader reader) throws IOException {
        final String line = readLine(reader);
        return line.split(SEPARATOR);
    }

    /**
     * Reads a single line from the buffer, trimming it of whitespaces if necessary
     */
    private static String readLine(final BufferedReader reader) throws IOException {
        return reader.readLine().trim();
    }
}
