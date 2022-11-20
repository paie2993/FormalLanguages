package org.compilers.finite_automaton.transitions;

import org.compilers.finite_automaton.Term;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.state.State;

import java.util.Set;

public interface Transition extends Term {

    State state();

    Symbol symbol();

    Set<State> resultStates();

    static Transition of(final String state, final String symbol, final Set<String> resultStates) {
        return new TransitionImpl(state, symbol, resultStates);
    }
}
