package org.compilers.finite_automaton.finite_automaton.domain.transitions;

import org.compilers.finite_automaton.automaton_io.automaton_reader.rules.RegexRules;
import org.compilers.finite_automaton.finite_automaton.domain.Term;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.alphabet.Symbol;
import org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state.State;

import java.util.Set;
import java.util.stream.Collectors;

public final class TransitionImpl implements Transition {

    private final State state;
    private final Symbol symbol;
    private final Set<State> resultStates;

    public TransitionImpl(final State state, final Symbol symbol, final Set<State> resultStates) {
        this.state = state;
        this.symbol = symbol;
        this.resultStates = resultStates;
    }

    @Override
    public State state() {
        return state;
    }

    @Override
    public Symbol symbol() {
        return symbol;
    }

    @Override
    public Set<State> resultStates() {
        return resultStates;
    }

    @Override
    public String representation() {
        return RegexRules.toSimpleString(RegexRules.TransitionRules.transitionPrefix) +
                state.representation() +
                RegexRules.toSimpleString(RegexRules.TransitionRules.argumentsSeparator) +
                symbol.representation() +
                RegexRules.toSimpleString(RegexRules.TransitionRules.transitionSuffix) +
                RegexRules.toSimpleString(RegexRules.TransitionRules.transitionSidesSeparator) +
                RegexRules.toSimpleString(RegexRules.TransitionRules.resultStatesPrefix) +
                resultStatesRepresentation() +
                RegexRules.toSimpleString(RegexRules.TransitionRules.resultStatesSuffix);
    }

    @Override
    public String toString() {
        return representation();
    }

    private String resultStatesRepresentation() {
        return resultStates.stream()
                .map(Term::representation)
                .collect(Collectors.joining(RegexRules.TransitionRules.resultStatesSeparator));
    }
}
