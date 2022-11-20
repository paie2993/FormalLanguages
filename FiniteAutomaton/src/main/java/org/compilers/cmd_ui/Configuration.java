package org.compilers.automaton_io.cmd_ui;

import org.compilers.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.implementation.DeterministicFiniteAutomaton;

final class Configuration {

    static final String FINITE_AUTOMATON_DEFINITION_FILE = "src/main/java/org/compilers/automaton_io/automata_files/one.txt";
    static final Class<? extends FiniteAutomaton> finiteAutomatonClass = DeterministicFiniteAutomaton.class;
}
