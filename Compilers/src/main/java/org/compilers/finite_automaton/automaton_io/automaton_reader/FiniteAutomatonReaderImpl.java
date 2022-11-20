package org.compilers.finite_automaton.automaton_io.automaton_reader;

import org.compilers.finite_automaton.automaton_io.automaton_reader.rules.RegexRules;
import org.compilers.finite_automaton.automaton_io.automaton_reader.transition_reader.TransitionBuilderImpl;
import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;
import org.compilers.finite_automaton.finite_automaton.domain.transitions.Transition;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.SymbolImpl;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.StateImpl;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public final class FiniteAutomatonReaderImpl implements FiniteAutomatonReader {

    // singleton implementation ...
    private FiniteAutomatonReaderImpl() {
    }

    private static final class LazyHolder {
        private static final FiniteAutomatonReaderImpl instance = new FiniteAutomatonReaderImpl();
    }

    public static FiniteAutomatonReader instance() {
        return LazyHolder.instance;
    }
    // ... ends here


    public FiniteAutomaton read(
            final String fileName,
            final Class<? extends FiniteAutomaton> finiteAutomatonClass
    ) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (final BufferedReader reader = openReader(fileName)) {
            final Set<State> states = buildStates(readBatch(reader));
            final Set<Symbol> alphabet = buildSymbols(readBatch(reader));
            final Set<Transition> transitions = buildTransitions(readBatch(reader));
            final State initialState = buildInitialState(readBatch(reader));
            final Set<State> finalStates = buildStates(readBatch(reader));
            return constructFiniteAutomaton(finiteAutomatonClass, states, alphabet, transitions, initialState, finalStates);
        }
    }

    private static Collection<String> readBatch(final BufferedReader reader) throws IOException {
        final Collection<String> batch = new LinkedList<>();

        String element = readElement(reader);
        while (!RegexRules.FileRules.batchSeparator.equals(element)) {
            batch.add(element);
            element = readElement(reader);
        }

        return batch;
    }


    private static Set<State> buildStates(final Collection<String> batch) {
        return batch.stream().map(StateImpl::new).collect(Collectors.toSet());
    }

    private Set<Symbol> buildSymbols(final Collection<String> batch) {
        return batch.stream().map(SymbolImpl::new).collect(Collectors.toSet());
    }

    private Set<Transition> buildTransitions(final Collection<String> batch) {
        return batch.stream().map(TransitionBuilderImpl.instance()::toTransition).collect(Collectors.toSet());
    }

    private State buildInitialState(final Collection<String> batch) {
        assertSingularInitialState(batch);
        final Optional<State> initialState = batch.stream().findFirst().map(StateImpl::new);
        if (initialState.isPresent()) {
            return initialState.get();
        }
        throw new IllegalArgumentException("Error while building initial state");
    }

    private static void assertSingularInitialState(final Collection<String> states) {
        if (states.size() != 1) {
            throw new IllegalArgumentException("There is more than one initial state: " + states);
        }
    }


    // general use functions for input from file
    private static String readElement(final BufferedReader reader) throws IOException {
        return reader.readLine().trim();
    }

    private static BufferedReader openReader(final String fileName) throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileName));
    }

    // construction of Finite Automaton using reflection
    private static FiniteAutomaton constructFiniteAutomaton(
            final Class<? extends FiniteAutomaton> finiteAutomatonClass,
            final Set<State> states,
            final Set<Symbol> alphabet,
            final Set<Transition> transitions,
            final State initialState,
            final Set<State> finalStates
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Constructor<? extends FiniteAutomaton> constructor =
                finiteAutomatonClass.getConstructor(Set.class, Set.class, Set.class, State.class, Set.class);

        return constructor.newInstance(states, alphabet, transitions, initialState, finalStates);
    }
}
