package org.compilers.grammar.model.vocab.terminal;

import org.compilers.grammar.model.vocab.Vocab;

import java.util.Objects;

public final class Terminal implements Vocab {

    private final String value;

    public Terminal(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isNonTerminal() {
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}
