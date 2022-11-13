package org.compilers.finite_automaton.consistency_checks;

import org.compilers.finite_automaton.transitions.Transition;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.state.State;

import java.util.Set;

public abstract class FiniteAutomatonConsistencyChecker {

    // to be implemented
    protected abstract void assertTransitionConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Transition transition
    );


    public final void assertConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) {
        assertTransitionsConsistency(states, alphabet, transitions);
        assertInitialStateConsistency(states, initialState);
        assertFinalStatesConsistency(states, finalStates);
    }

    // transitions
    private void assertTransitionsConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions
    ) {
        for (final Transition transition : transitions) {
            assertTransitionConsistency(states, alphabet, transition);
        }
    }


    // initial state consistency
    private static void assertInitialStateConsistency(
            final Set<State> states,
            final State initialState
    ) {
        if (!states.contains(initialState)) {
            throw new IllegalArgumentException("Initial state not in set of states: " + initialState.representation());
        }
    }

    // final states consistency
    private static void assertFinalStatesConsistency(
            final Set<State> states,
            final Set<State> finalStates
    ) {
        for (final State finalState : finalStates) {
            assertFinalStateConsistency(states, finalState);
        }
    }

    private static void assertFinalStateConsistency(
            final Set<State> states,
            final State finalState
    ) {
        if (!states.contains(finalState)) {
            throw new IllegalArgumentException("Final state not in set of states: " + finalState.representation());
        }
    }
}
