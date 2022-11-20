package org.compilers.finite_automaton.automaton_io.automaton_reader.transition_reader;

import org.compilers.finite_automaton.automaton_io.automaton_reader.rules.RegexRules;
import org.compilers.finite_automaton.finite_automaton.domain.transitions.Transition;
import org.compilers.finite_automaton.finite_automaton.domain.transitions.TransitionImpl;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.SymbolImpl;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.StateImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class TransitionBuilderImpl implements TransitionBuilder {

    // singleton implementation ...
    private TransitionBuilderImpl() {
    }

    private static final class LazyHolder {
        private static final TransitionBuilderImpl instance = new TransitionBuilderImpl();
    }

    public static TransitionBuilderImpl instance() {
        return LazyHolder.instance;
    }
    // ... ends here


    private static final Pattern transitionPattern = Pattern.compile(RegexRules.TransitionRules.transitionRule);


    @Override
    public Transition toTransition(final String string) {
        assertTransitionSpecification(string);
        final TransitionSides sides = getLeftAndRightHandSides(string);
        final TransitionArguments arguments = getTransitionArguments(sides.left);
        final TransitionResults results = getTransitionResults(sides.right);
        return buildTransition(arguments, results);
    }

    private static void assertTransitionSpecification(final String string) {
        final Matcher matcher = transitionPattern.matcher(string);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Illegal transition specification: " + string);
        }
    }


    private static TransitionSides getLeftAndRightHandSides(final String string) {
        final String[] sides = string.split(RegexRules.TransitionRules.transitionSidesSeparator);
        return new TransitionSides(sides[0], sides[1]);
    }

    private static final record TransitionSides(String left, String right) {
    }


    private static TransitionArguments getTransitionArguments(final String string) {
        final String prefix = RegexRules.TransitionRules.transitionPrefix;
        final String suffix = RegexRules.TransitionRules.transitionSuffix;
        final String separator = RegexRules.TransitionRules.argumentsSeparator;
        final List<String> arguments = extractTokens(string, prefix, suffix, separator);
        return new TransitionArguments(arguments.get(0), arguments.get(1));
    }

    private static final record TransitionArguments(String state, String symbol) {
    }


    private static TransitionResults getTransitionResults(final String string) {
        final String prefix = RegexRules.TransitionRules.resultStatesPrefix;
        final String suffix = RegexRules.TransitionRules.resultStatesSuffix;
        final String separator = RegexRules.TransitionRules.resultStatesSeparator;
        final Collection<String> resultStates = extractTokens(string, prefix, suffix, separator);
        return new TransitionResults(resultStates);
    }

    private static final record TransitionResults(Collection<String> states) {
    }


    private static List<String> extractTokens(final String string, final String prefix, final String suffix, final String separator) {
        final String withoutAffixes = dropAffixes(string, prefix, suffix);
        final String[] tokens = withoutAffixes.split(separator);
        return Arrays.stream(tokens).toList();
    }

    private static String dropAffixes(final String string, final String prefix, final String suffix) {
        return string.replaceAll(prefix, "").replaceAll(suffix, "");
    }


    private static Transition buildTransition(final TransitionArguments arguments, final TransitionResults results) {
        final State state = new StateImpl(arguments.state);
        final Symbol symbol = new SymbolImpl(arguments.symbol);
        final Set<State> resultStates = buildResultStates(results.states);
        return new TransitionImpl(state, symbol, resultStates);
    }

    private static Set<State> buildResultStates(final Collection<String> states) {
        return states.stream().map(StateImpl::new).collect(Collectors.toSet());
    }
}
