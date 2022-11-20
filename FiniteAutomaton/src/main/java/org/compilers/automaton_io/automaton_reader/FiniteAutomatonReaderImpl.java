package org.compilers.automaton_io.automaton_reader;

import org.compilers.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.implementation.DeterministicFiniteAutomatonImpl;
import org.compilers.finite_automaton.transitions.Transition;
import org.compilers.finite_automaton.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.vocabulary.alphabet.SymbolImpl;
import org.compilers.finite_automaton.vocabulary.state.State;
import org.compilers.finite_automaton.vocabulary.state.StateImpl;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DeterministicFiniteAutomatonReaderImpl implements FiniteAutomatonReader {

    // singleton implementation ...
    private DeterministicFiniteAutomatonReaderImpl() {
    }

    private static final class LazyHolder {
        private static final DeterministicFiniteAutomatonReaderImpl instance = new DeterministicFiniteAutomatonReaderImpl();
    }

    public static FiniteAutomatonReader instance() {
        return LazyHolder.instance;
    }
    // ... ends here


    private static final Pattern transitionPattern = Pattern.compile(RegexRules.TransitionRules.transitionRule);


    public FiniteAutomaton read(final String fileName, final String stop) throws IOException {
        try (final BufferedReader reader = openReader(fileName)) {
            final Set<State> states = readStates(reader, stop);
            final Set<Symbol> alphabet = readAlphabet(reader, stop);
            final Set<Transition> transitions = readTransitions(reader, stop);
            final State initialState = readInitialState(reader, stop);
            final Set<State> finalStates = readStates(reader, stop);
            return DeterministicFiniteAutomatonImpl.of(states, alphabet, transitions, initialState, finalStates);
        }
    }

    private static Collection<String> readBatch(final BufferedReader reader, final String stop) throws IOException {
        final Collection<String> batch = new LinkedList<>();

        String element = readElement(reader);
        while (!stop.equals(element)) {
            batch.add(element);
            element = readElement(reader);
        }

        return batch;
    }

    private static Set<State> buildStates(final Collection<String> batch) {
        return batch.stream().map(StateImpl::new).collect(Collectors.toSet());
    }

    /**
     * reads the set of states
     */
    private static Set<State> readStates(final BufferedReader reader, final String stop) throws IOException {
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
        return string.split(RegexRules.TransitionRules.resultStatesSeparator);
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
