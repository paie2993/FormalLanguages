package org.compilers.grammar.model;

import org.compilers.grammar.model.vocab.non_terminal.NonTerminal;
import org.compilers.grammar.model.production.Production;
import org.compilers.grammar.model.vocab.terminal.Terminal;
import org.compilers.grammar.model.vocab.Vocab;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class Grammar {


    public static final String EPSILON = "epsilon";

    private final Set<NonTerminal> nonTerminals;

    private final Set<Terminal> terminals;

    private final Set<Production> productions;

    private final NonTerminal startingNonTerminal;


    public Grammar(
            final Set<NonTerminal> nonTerminals,
            final Set<Terminal> terminals,
            final Set<Production> productions,
            final NonTerminal startingNonTerminal
    ) {
        Objects.requireNonNull(nonTerminals);
        Objects.requireNonNull(terminals);
        Objects.requireNonNull(productions);
        Objects.requireNonNull(startingNonTerminal);
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.startingNonTerminal = startingNonTerminal;
    }

    public Set<NonTerminal> nonTerminals() {
        return nonTerminals;
    }

    public Set<Terminal> terminals() {
        return terminals;
    }

    public Set<Production> productions() {
        return productions;
    }

    public NonTerminal startingNonTerminal() {
        return startingNonTerminal;
    }


    /**
     * Returns all the productions of the given non-terminal
     * If the non-terminal is not part of the grammar, returns empty set
     */
    public Set<Production> productions(final NonTerminal nonTerminal) {
        final var cfgProductions =
                productions.stream().filter(it -> it.left().size() == 1).collect(Collectors.toUnmodifiableSet());

        return cfgProductions.stream().filter(it -> nonTerminal.equals(it.left().get(0))).collect(Collectors.toUnmodifiableSet());
    }


    /**
     * Checks if the given non-terminal belongs to the set of non-terminals
     */
    public boolean containsNonTerminal(final NonTerminal nonTerminal) {
        return nonTerminals.contains(nonTerminal);
    }


    /**
     * Checks if the grammar is context free
     */
    public boolean isContextFree() {
        for (final Production production : productions) {
            if (!isContextFreeProduction(production)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the production can belong to a context free grammar (has only one terminal on in the left side)
     */
    private static boolean isContextFreeProduction(final Production production) {
        final List<Vocab> left = production.left();

        if (left.size() > 1) {
            return false;
        }

        final Vocab vocab = left.get(0);
        return vocab.isNonTerminal();
    }
}
