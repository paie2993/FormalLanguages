package org.compilers.automaton_io.automaton_reader;

import org.compilers.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.implementation.DeterministicFiniteAutomatonImpl;
import org.compilers.finite_automaton.transitions.Transition;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.alphabet.SymbolImpl;
import org.compilers.finite_automaton.vocabulary.state.State;
import org.compilers.finite_automaton.vocabulary.state.StateImpl;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DeterministicFiniteAutomatonReaderImpl implements FiniteAutomatonReader {

    private DeterministicFiniteAutomatonReaderImpl() {
    }

    private static final class LazyHolder {
        private static final DeterministicFiniteAutomatonReaderImpl instance = new DeterministicFiniteAutomatonReaderImpl();
    }

    public static FiniteAutomatonReader instance() {
        return LazyHolder.instance;
    }


    private static final Pattern transitionPattern = Pattern.compile(RegexRules.TransitionRules.completeRule);

    public FiniteAutomaton read(final String fileName) throws IOException {
        try (final BufferedReader reader = openReader(fileName)) {
            final Set<State> states = readStates(reader);
            final Set<Symbol> alphabet = readAlphabet(reader);
            final Set<Transition> transitions = readTransitions(reader);
            final State initialState = readInitialState(reader);
            final Set<State> finalStates = readStates(reader);
            return DeterministicFiniteAutomatonImpl.of(states, alphabet, transitions, initialState, finalStates);
        }
    }


    // read the various elements of a finite automaton
    private static Set<State> readStates(final BufferedReader reader) throws IOException {
        final Set<State> states = new HashSet<>();

        String element = readElement(reader);
        while (!element.isEmpty()) {
            states.add(new StateImpl(element));
            element = readElement(reader);
        }

        return states;
    }

    private static Set<Symbol> readAlphabet(final BufferedReader reader) throws IOException {
        final Set<Symbol> alphabet = new HashSet<>();

        String element = readElement(reader);
        while (!element.isEmpty()) {
            alphabet.add(new SymbolImpl(element));
            element = readElement(reader);
        }

        return alphabet;
    }

    private static Set<Transition> readTransitions(final BufferedReader reader) throws IOException {
        final Set<Transition> transitions = new HashSet<>();

        String element = readElement(reader);
        while (!element.isEmpty()) {

            final Transition transition = buildTransitionFromString(element);
            transitions.add(transition);

            element = readElement(reader);
        }
        return transitions;
    }

    private static State readInitialState(final BufferedReader reader) throws IOException {
        final String element = readElement(reader);
        final State initialState = new StateImpl(element);
        reader.readLine();
        return initialState;
    }

    // processing transitions
    private static Transition buildTransitionFromString(final String string) {
        final String[] tokens = extractTransitionTokens(string);

        final String resultStatesString = tokens[2];
        final String[] resultStatesTokens = extractResultStatesTokens(resultStatesString);
        final Set<String> resultStates = arrayToSet(resultStatesTokens);

        return Transition.of(tokens[0], tokens[1], resultStates);
    }

    private static String[] extractTransitionTokens(final String string) {
        assertTransitionSpecification(string);
        final String trimmedTransition = string.substring(2, string.length() - 2);
        return trimmedTransition.split(", |\\) = \\{ ");
    }

    private static String[] extractResultStatesTokens(final String string) {
        return string.split(RegexRules.TransitionRules.statesSetElementsSeparator);
    }

    private static <T> Set<T> arrayToSet(final T[] array) {
        return Arrays.stream(array).collect(Collectors.toUnmodifiableSet());
    }

    private static void assertTransitionSpecification(final String transition) {
        final Matcher matcher = transitionPattern.matcher(transition);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Illegal transition specification: " + transition);
        }
    }

    // general use functions for input from file
    private static String readElement(final BufferedReader reader) throws IOException {
        return reader.readLine().trim();
    }

    private static BufferedReader openReader(final String fileName) throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileName));
    }
}
