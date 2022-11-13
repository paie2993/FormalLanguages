package org.compilers.finite_automaton.implementation;

import org.compilers.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.transitions.Transition;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.state.State;

import java.util.Set;

public abstract class FiniteAutomatonImpl implements FiniteAutomaton {

    private final Set<State> states;
    private final Set<Symbol> alphabet;
    private final Set<Transition> transitions;
    private final State initialState;
    private final Set<State> finalStates;


    protected FiniteAutomatonImpl(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) {
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
}
