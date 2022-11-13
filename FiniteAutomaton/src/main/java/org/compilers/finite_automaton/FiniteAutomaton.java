package org.compilers.finite_automaton;

import org.compilers.finite_automaton.transitions.Transition;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.state.State;

import java.util.Set;

public interface FiniteAutomaton {

    Set<State> states();

    Set<Symbol> alphabet();

    Set<Transition> transitions();

    State initialState();

    Set<State> finalStates();
}
