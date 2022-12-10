package org.compilers.grammar.model.production;

import org.compilers.grammar.model.Grammar;
import org.compilers.grammar.model.GrammarReader;
import org.compilers.grammar.model.vocab.non_terminal.NonTerminal;
import org.compilers.grammar.model.vocab.terminal.Terminal;
import org.compilers.grammar.model.vocab.Vocab;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ProductionBuilder {

    private static final String SIDES_SEPARATOR = " -> ";
    private static final String SYMBOLS_SEPARATOR = "`";

    private final Convention convention;

    public ProductionBuilder(final Set<NonTerminal> nonTerminals, final Set<Terminal> terminals) {
        this.convention = new Convention(nonTerminals, terminals);
    }

    public Production buildProduction(final String productionString) {
        final String[] sides = productionString.split(SIDES_SEPARATOR);

        final List<Vocab> left = buildLeftSide(sides[0]);
        final List<Vocab> right = buildRightSide(sides[1]);

        return new Production(left, right);
    }

    /**
     * Builds the left side of the production
     */
    private List<Vocab> buildLeftSide(final String sideString) {
        return buildSide(sideString);
    }

    /**
     * Builds the right side of the production, with the special case of empty production
     */
    private List<Vocab> buildRightSide(final String sideString) {
        final var epsilonOptional = specialCase(sideString);
        if (epsilonOptional.isPresent()) {
            return List.of(epsilonOptional.get());
        }
        return buildSide(sideString);
    }

    private Optional<Terminal> specialCase(final String rightSide) {
        if (Grammar.EPSILON.equals(rightSide)) {
            return Optional.of(new Terminal(Grammar.EPSILON));
        }
        return Optional.empty();
    }

    private List<Vocab> buildSide(final String sideString) {
        final String[] symbols = processSymbols(sideString);
        return convertSymbols(symbols);
    }

    /**
     * Receives one side of the production, and splits the vocabs according to [SYMBOLS_SEPARATOR]
     */
    private String[] processSymbols(final String sideString) {
        return sideString.split(SYMBOLS_SEPARATOR);
    }

    /**
     * Converts an array of vocab representations to their corresponding terminals and non-terminals
     */
    private List<Vocab> convertSymbols(final String[] symbols) {
        return Arrays.stream(symbols).map(this::convertSymbol).collect(Collectors.toList());
    }

    private Vocab convertSymbol(final String symbol) {
        final Function<String, Vocab> converter = convention.getConverter(symbol);
        return converter.apply(symbol);
    }

    public static final class Convention {

        private final Set<String> nonTerminalsValues;
        private final Set<String> terminalsValues;

        public Convention(final Set<NonTerminal> nonTerminals, final Set<Terminal> terminals) {
            this.terminalsValues = terminals.stream().map(Terminal::value).collect(Collectors.toUnmodifiableSet()); // unbound
            this.nonTerminalsValues = nonTerminals.stream().map(NonTerminal::value).collect(Collectors.toUnmodifiableSet()); // unbound
        }

        private Function<String, Vocab> getConverter(final String string) {
            if (terminalsValues.contains(string)) { // terminal
                return Terminal::new;
            } else if (nonTerminalsValues.contains(string)) { // non-terminal
                return NonTerminal::new;
            }
            throw new IllegalArgumentException("The string " + string + " does not follow any convention");
        }
    }
}
