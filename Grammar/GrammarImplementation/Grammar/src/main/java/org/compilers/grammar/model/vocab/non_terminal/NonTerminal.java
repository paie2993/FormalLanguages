package org.compilers.grammar.model.vocab.non_terminal;

import org.compilers.grammar.model.vocab.Vocab;

import java.util.Objects;

public final class NonTerminal implements Vocab {

    private final String value;

    public NonTerminal(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isNonTerminal() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof NonTerminal other)) {
            return false;
        }
        return other.value.equals(this.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
