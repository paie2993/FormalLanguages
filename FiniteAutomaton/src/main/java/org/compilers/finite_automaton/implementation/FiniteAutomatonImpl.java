package org.compilers.finite_automaton.implementation;

import org.compilers.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.transitions.Transition;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.state.State;

import java.util.Set;

public final class FiniteAutomatonImpl implements FiniteAutomaton {

    private final Set<State> states;
    private final Set<Symbol> alphabet;
    private final Set<Transition> transitions;
    private final State initialState;
    private final Set<State> finalStates;

    public FiniteAutomatonImpl(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) {
        assertConsistency(states, alphabet, transitions, initialState, finalStates);
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initialState = initialState;
        this.finalStates = finalStates;
    }

    @Override
    public Set<State> states() {
        return states;
    }

    @Override
    public Set<Symbol> alphabet() {
        return alphabet;
    }

    @Override
    public Set<Transition> transitions() {
        return transitions;
    }

    @Override
    public State initialState() {
        return initialState;
    }

    @Override
    public Set<State> finalStates() {
        return finalStates;
    }

    private static void assertConsistency(
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
    private static void assertTransitionsConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions
    ) {
        for (final Transition transition : transitions) {
            assertTransitionConsistency(states, alphabet, transition);
        }
    }

    private static void assertTransitionConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Transition transition
    ) {
        final State state = transition.state();
        final Symbol symbol = transition.symbol();
        final State resultState = transition.resultState();

        if (!states.contains(state)) {
            throw new IllegalArgumentException("State of transition not in set of states: " + state.representation());
        }
        if (!alphabet.contains(symbol)) {
            throw new IllegalArgumentException("Symbol of transition not in alphabet: " + symbol.representation());
        }
        if (!states.contains(resultState)) {
            throw new IllegalArgumentException("Result state of transition not in set of states: " + resultState.representation());
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
