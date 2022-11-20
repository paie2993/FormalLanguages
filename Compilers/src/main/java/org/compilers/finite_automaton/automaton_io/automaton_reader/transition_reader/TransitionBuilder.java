package org.compilers.finite_automaton.automaton_io.automaton_reader.transition_reader;

import org.compilers.finite_automaton.finite_automaton.domain.transitions.Transition;

public interface TransitionBuilder {

    Transition toTransition(final String string);
}
