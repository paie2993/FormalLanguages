package org.compilers.configuration;

import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.finite_automaton.implementation.DeterministicFiniteAutomaton;

public final class Configuration {

    public static final String TOKENS_FILE = "src/main/java/org/compilers/configuration/static/Tokens.txt";

    public static final String PROGRAM_INPUT_FILE = "src/main/java/org/compilers/scanner/files/programs/P1.txt";
    public static final String SYMBOL_TABLE_OUTPUT_FILE = "src/main/java/org/compilers/scanner/files/output/symbol.txt";
    public static final String PIF_OUTPUT_FILE = "src/main/java/org/compilers/scanner/files/output/internal.txt";

    public static final String FINITE_AUTOMATON_DEFINITION_FILE = "src/main/java/org/compilers/finite_automaton/automaton_io/automata_files/one.txt";
    public static final String IDENTIFIER_FINITE_AUTOMATON_FILE = "src/main/java/org/compilers/finite_automaton/automaton_io/automata_files/identifier.txt";
    public static final String INTEGER_FINITE_AUTOMATON_FILE = "src/main/java/org/compilers/finite_automaton/automaton_io/automata_files/integer.txt";

    public static final Class<? extends FiniteAutomaton> FINITE_AUTOMATON_CLASS = DeterministicFiniteAutomaton.class;
}
