package org.compilers.finite_automaton.finite_automaton.implementation;

import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.finite_automaton.domain.transitions.Transition;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;

import java.util.Optional;
import java.util.Set;

public abstract class FiniteAutomatonBase implements FiniteAutomaton {

    private final Set<State> states;
    private final Set<Symbol> alphabet;
    private final Set<Transition> transitions;
    private final State initialState;
    private final Set<State> finalStates;


    protected FiniteAutomatonBase(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) {
        checkConsistency(states, alphabet, transitions, initialState, finalStates);
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initialState = initialState;
        this.finalStates = finalStates;
    }

    protected abstract void checkConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    );

    @Override
    public final Set<State> states() {
        return states;
    }

    @Override
    public final Set<Symbol> alphabet() {
        return alphabet;
    }

    @Override
    public final Set<Transition> transitions() {
        return transitions;
    }

    @Override
    public Optional<Transition> transition(final State state, final Symbol symbol) {
        return transitions.stream()
                .filter(transition -> matches(transition, state, symbol))
                .findFirst();
    }

    @Override
    public final State initialState() {
        return initialState;
    }

    @Override
    public final Set<State> finalStates() {
        return finalStates;
    }

    private static boolean matches(final Transition transition, final State state, final Symbol symbol) {
        return state.equals(transition.state()) && symbol.equals(transition.symbol());
    }
}
