package org.compilers.finite_automaton.vocabulary.alphabet;

public final class SymbolImpl implements Symbol {

    private final String symbol;

    public SymbolImpl(final String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String representation() {
        return symbol;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof SymbolImpl oSymbol)) {
            return false;
        }
        return this.symbol.equals(oSymbol.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    @Override
    public String toString() {
        return representation();
    }
}
