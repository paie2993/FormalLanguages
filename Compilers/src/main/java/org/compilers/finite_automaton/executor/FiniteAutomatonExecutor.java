package org.compilers.finite_automaton.executor;

import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;

public interface FiniteAutomatonExecutor {

    boolean apply(final FiniteAutomaton finiteAutomaton, final String sequence);
}
