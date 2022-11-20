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

    /**
     * check that the set of states and the alphabet are in accordance with the transition rules, the initial state and
     * the final states
     */
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

    /**
     * check if the transition rules use states and symbols that appear in the set of states, respectively in the alphabet
     */
    private void assertTransitionsConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions
    ) {
        for (final Transition transition : transitions) {
            assertTransitionConsistency(states, alphabet, transition);
        }
    }


    /**
     * check if the initial state is in the set of states
     */
    private static void assertInitialStateConsistency(
            final Set<State> states,
            final State initialState
    ) {
        if (!states.contains(initialState)) {
            throw new IllegalArgumentException("Initial state not in set of states: " + initialState.representation());
        }
    }


    /**
     * check if all the final states appear in the set of states
     */
    private static void assertFinalStatesConsistency(
            final Set<State> states,
            final Set<State> finalStates
    ) {
        for (final State finalState : finalStates) {
            assertFinalStateConsistency(states, finalState);
        }
    }

    /**
     * check if one of the final states is in the set of states
     */
    private static void assertFinalStateConsistency(
            final Set<State> states,
            final State finalState
    ) {
        if (!states.contains(finalState)) {
            throw new IllegalArgumentException("Final state not in set of states: " + finalState.representation());
        }
    }
}
