package org.compilers.finite_automaton.finite_automaton.implementation;

import org.compilers.finite_automaton.finite_automaton.consistency_checks.DeterministicFiniteAutomatonConsistencyChecker;
import org.compilers.finite_automaton.finite_automaton.domain.transitions.Transition;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;

import java.util.Set;

public final class DeterministicFiniteAutomaton extends FiniteAutomatonBase {

    public DeterministicFiniteAutomaton(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) {
        super(states, alphabet, transitions, initialState, finalStates);
    }

    @Override
    protected void checkConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) {
        var checker = DeterministicFiniteAutomatonConsistencyChecker.instance();
        checker.assertConsistency(states, alphabet, transitions, initialState, finalStates);
    }
}
