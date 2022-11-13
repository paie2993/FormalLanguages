package org.compilers.automaton_io.automaton_reader;

import org.compilers.finite_automaton.FiniteAutomaton;

import java.io.IOException;

public interface FiniteAutomatonReader {

    FiniteAutomaton read(final String fileName) throws IOException;
}
