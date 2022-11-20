package org.compilers.finite_automaton.automaton_io.automaton_reader;

import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface FiniteAutomatonReader {

    FiniteAutomaton read(final String fileName, final Class<? extends FiniteAutomaton> finiteAutomatonClass)
            throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
