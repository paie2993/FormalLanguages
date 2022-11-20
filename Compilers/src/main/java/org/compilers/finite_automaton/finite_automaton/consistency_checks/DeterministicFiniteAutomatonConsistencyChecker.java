package org.compilers.finite_automaton.finite_automaton.consistency_checks;

import org.compilers.finite_automaton.finite_automaton.domain.transitions.Transition;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;

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
        assertTransitionStateConsistency(states, transition);
        assertTransitionSymbolConsistency(alphabet, transition);
        assertSingularResultStateConsistency(transition);
        assertResultStateConsistency(states, transition);
    }

    /**
     * checks that the number of final states is exactly one
     */
    private void assertSingularResultStateConsistency(
            final Transition transition
    ) {
        final Set<State> resultStates = transition.resultStates();
        if (resultStates.size() != 1) {
            throw new IllegalArgumentException("Several result states specified for the transition: " + resultStates);
        }
    }

    /**
     * extracts the result states from the set of result states of the transition (there should be only one result state)
     */
    private State getResultState(final Set<State> resultStates) {
        final Optional<State> resultState = resultStates.stream().findFirst();
        if (resultState.isEmpty()) {
            throw new RuntimeException("No result state");
        }
        return resultState.get();
    }

    /**
     * checks that the final state is in the set of states
     */
    private void assertResultStateConsistency(
            final Set<State> states,
            final Transition transition
    ) {
        final State resultState = getResultState(transition.resultStates());
        if (!states.contains(resultState)) {
            throw new IllegalArgumentException("Result state not in set of states: " + resultState);
        }
    }
}
