package org.compilers.finite_automaton.executor;

import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.finite_automaton.domain.transitions.Transition;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.SymbolImpl;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;

import java.util.Optional;
import java.util.Set;

public final class FiniteAutomatonExecutorImpl implements FiniteAutomatonExecutor {

    // singleton implementation ...
    private FiniteAutomatonExecutorImpl() {
    }

    private static final class LazyHolder {
        private static final FiniteAutomatonExecutorImpl instance = new FiniteAutomatonExecutorImpl();
    }

    public static FiniteAutomatonExecutorImpl instance() {
        return LazyHolder.instance;
    }
    // ... ends here

    // TODO: should be more generic, such that it could execute also NFAs.
    @Override
    public boolean apply(final FiniteAutomaton finiteAutomaton, final String sequence) {

        State currentState = finiteAutomaton.initialState();
        String currentSequence = sequence;

        while (!isDone(currentSequence)) {
            final Symbol currentSymbol = nextSymbol(currentSequence);
            currentSequence = advanceSequence(currentSequence);

            final Optional<Transition> transition = finiteAutomaton.transition(currentState, currentSymbol);
            if (transition.isEmpty()) {
                return false;
            }

            currentState = getResultState(transition.get());
        }

        return accepts(finiteAutomaton, currentState);
    }

    private static State getResultState(final Transition transition) {
        final Optional<State> resultState = transition.resultStates().stream().findFirst();
        if (resultState.isEmpty()) {
            throw new IllegalStateException("Transition without result states: " + transition);
        }
        return resultState.get();
    }

    private static Symbol nextSymbol(final String sequence) {
        final char firstChar = sequence.charAt(0);
        final String charAsString = String.valueOf(firstChar);
        return new SymbolImpl(charAsString);
    }

    private static String advanceSequence(final String sequence) {
        return sequence.substring(1);
    }

    private static boolean isDone(final String sequence) {
        return sequence.isEmpty();
    }

    private static boolean accepts(final FiniteAutomaton finiteAutomaton, final State currentState) {
        final Set<State> finalStates = finiteAutomaton.finalStates();
        return finalStates.contains(currentState);
    }
}
