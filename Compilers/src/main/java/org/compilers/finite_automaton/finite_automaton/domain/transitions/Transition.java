package org.compilers.finite_automaton.finite_automaton.domain.transitions;

import org.compilers.finite_automaton.finite_automaton.domain.Term;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;

import java.util.Set;

public interface Transition extends Term {

    State state();

    Symbol symbol();

    Set<State> resultStates();
}
