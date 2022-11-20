package org.compilers.finite_automaton.finite_automaton;

import org.compilers.finite_automaton.finite_automaton.domain.transitions.Transition;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;

import java.util.Optional;
import java.util.Set;

public interface FiniteAutomaton {

    Set<State> states();

    Set<Symbol> alphabet();

    Set<Transition> transitions();

    Optional<Transition> transition(final State state, final Symbol symbol);

    State initialState();

    Set<State> finalStates();
}
