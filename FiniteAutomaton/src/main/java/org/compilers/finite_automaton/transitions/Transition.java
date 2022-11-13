package org.compilers.finite_automaton.transitions;

import org.compilers.finite_automaton.Term;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.state.State;

public interface Transition extends Term {

    State state();

    Symbol symbol();

    State resultState();

    static Transition of(final String state, final String symbol, final String resultState) {
        return new TransitionImpl(state, symbol, resultState);
    }
}
