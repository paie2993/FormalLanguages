package org.compilers.finite_automaton.consistency_checks;

import org.compilers.finite_automaton.transitions.Transition;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.state.State;

import java.util.Optional;
import java.util.Set;

public final class DeterministicFiniteAutomatonConsistencyChecker extends FiniteAutomatonConsistencyChecker {

    private DeterministicFiniteAutomatonConsistencyChecker() {
    }

    private static final class LazyHolder {
        private static final DeterministicFiniteAutomatonConsistencyChecker instance = new DeterministicFiniteAutomatonConsistencyChecker();
    }

    public static DeterministicFiniteAutomatonConsistencyChecker instance() {
        return LazyHolder.instance;
    }


    @Override
    protected void assertTransitionConsistency(
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Transition transition
    ) {
        final State state = transition.state();
        final Symbol symbol = transition.symbol();
        final Set<State> resultStates = transition.resultStates();

        if (!states.contains(state)) {
            throw new IllegalArgumentException("State of transition not in set of states: " + state.representation());
        }
        if (!alphabet.contains(symbol)) {
            throw new IllegalArgumentException("Symbol of transition not in alphabet: " + symbol.representation());
        }
        assertSingularResultStateConsistency(resultStates);

        final State resultState = getResultState(resultStates);
        assertResultStateConsistency(states, resultState);
    }

    private void assertSingularResultStateConsistency(
            final Set<State> resultStates
    ) {
        if (resultStates.size() != 1) {
            throw new IllegalArgumentException("Several result states specified for the transition: " + resultStates);
        }
    }

    private State getResultState(final Set<State> resultStates) {
        final Optional<State> resultState = resultStates.stream().findFirst();
        if (resultState.isEmpty()) {
            throw new RuntimeException("No result state");
        }
        return resultState.get();
    }

    private void assertResultStateConsistency(
            final Set<State> states,
            final State resultState
    ) {
        if (!states.contains(resultState)) {
            throw new IllegalArgumentException("Result state not in set of states: " + resultState);
        }
    }
}
