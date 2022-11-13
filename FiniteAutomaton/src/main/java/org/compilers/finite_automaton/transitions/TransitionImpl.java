package org.compilers.finite_automaton.transitions;

import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.alphabet.SymbolImpl;
import org.compilers.finite_automaton.vocabulary.state.State;
import org.compilers.finite_automaton.vocabulary.state.StateImpl;

public final class TransitionImpl implements Transition {

    private final State state;
    private final Symbol symbol;
    private final State resultState;

    TransitionImpl(final String state, final String symbol, final String resultState) {
        this.state = new StateImpl(state);
        this.symbol = new SymbolImpl(symbol);
        this.resultState = new StateImpl(resultState);
    }

    public TransitionImpl(final State state, final Symbol symbol, final State resultState) {
        this.state = state;
        this.symbol = symbol;
        this.resultState = resultState;
    }

    @Override
    public State state() {
        return state;
    }

    @Override
    public Symbol symbol() {
        return symbol;
    }

    @Override
    public State resultState() {
        return resultState;
    }

    @Override
    public String representation() {
        return "d(" +
                state.representation() +
                ", " +
                symbol.representation() +
                ")" +
                " = " +
                resultState.representation();
    }
}
