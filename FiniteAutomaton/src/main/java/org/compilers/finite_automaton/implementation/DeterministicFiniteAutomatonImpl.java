package org.compilers.finite_automaton.implementation;

import org.compilers.finite_automaton.consistency_checks.DeterministicFiniteAutomatonConsistencyChecker;
import org.compilers.finite_automaton.consistency_checks.FiniteAutomatonConsistencyChecker;
import org.compilers.finite_automaton.transitions.Transition;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.state.State;

import java.util.Set;

public final class DeterministicFiniteAutomatonImpl extends FiniteAutomatonImpl {

    private static final FiniteAutomatonConsistencyChecker checker = DeterministicFiniteAutomatonConsistencyChecker.instance();

    private DeterministicFiniteAutomatonImpl(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) {
        super(states, alphabet, transitions, initialState, finalStates);
    }

    public static DeterministicFiniteAutomatonImpl of(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) {
        checker.assertConsistency(states, alphabet, transitions, initialState, finalStates);
        return new DeterministicFiniteAutomatonImpl(states, alphabet, transitions, initialState, finalStates);
    }
}
