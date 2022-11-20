package org.compilers.scanner.analyser.matcher;

import org.compilers.finite_automaton.executor.FiniteAutomatonExecutorImpl;
import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.compilers.scanner.analyser.regex.RegexUtil.joinRegex;

public final class TokenMatcherImpl implements TokenMatcher {

    private int start;
    private int end;
    private boolean succeeded = false;

    private final String string;

    private final Matcher matcher;
    private final Set<FiniteAutomaton> finiteAutomata;

    public TokenMatcherImpl(final String string, final String... regex) {
        this(string, Arrays.stream(regex).collect(Collectors.toSet()), null);
    }

    public TokenMatcherImpl(final String string, final FiniteAutomaton... finiteAutomata) {
        this(string, null, Arrays.stream(finiteAutomata).collect(Collectors.toSet()));
    }

    public TokenMatcherImpl(final String string, final Set<String> regex, final Set<FiniteAutomaton> finiteAutomata) {
        if ((regex == null || regex.isEmpty()) && (finiteAutomata == null || finiteAutomata.isEmpty())) {
            throw new IllegalArgumentException("TokenMatcher should have at least one matching rule");
        }

        this.string = string;

        if (regex != null && !regex.isEmpty()) {
            this.matcher = Pattern.compile(joinRegex(regex)).matcher(string);
        } else {
            this.matcher = null;
        }

        if (finiteAutomata != null && !finiteAutomata.isEmpty()) {
            this.finiteAutomata = finiteAutomata;
        } else {
            this.finiteAutomata = new HashSet<>();
        }
    }

    @Override
    public int start() {
        if (succeeded) {
            return start;
        }
        throw new IllegalStateException("No match");
    }

    @Override
    public int end() {
        if (succeeded) {
            return end;
        }
        throw new IllegalStateException("No match");
    }

    @Override
    public boolean matches() {
        boolean matches = false;

        if (matcher != null) {
            matches = matcher.matches();
        }

        if (!matches && !finiteAutomata.isEmpty()) {
            matches = matchedFiniteAutomaton(string);
        }

        return matches;
    }

    @Override
    public boolean find(final int index) {
        ParsingInformation matcherInformation = null;
        ParsingInformation automataInformation = null;

        if (matcher != null) {
            matcherInformation = findWithMatcher(index);
        }

        if (!finiteAutomata.isEmpty()) {
            automataInformation = findWithFiniteAutomata(index);
        }

        final ParsingInformation match = chooseMatch(matcherInformation, automataInformation);
        if (match == null || !match.isSuccess()) {
            return setFailure();
        }
        return setSuccess(match.start, match.end);
    }

    private static ParsingInformation chooseMatch(final ParsingInformation matcherInformation, final ParsingInformation automatonInformation) {
        if (matcherInformation == null && automatonInformation == null) {
            return null;
        }

        if (matcherInformation != null && automatonInformation == null) {
            return matcherInformation;
        }

        if (matcherInformation == null) {
            return automatonInformation;
        }

        if (!matcherInformation.isSuccess() && !automatonInformation.isSuccess()) {
            return matcherInformation;
        }

        if (matcherInformation.isSuccess() && !automatonInformation.isSuccess()) {
            return matcherInformation;
        }

        if (!matcherInformation.isSuccess()) {
            return automatonInformation;
        }

        if (matcherInformation.start < automatonInformation.start) {
            return matcherInformation;
        }
        return automatonInformation;
    }

    private ParsingInformation findWithMatcher(final int index) {
        if (matcher.find(index)) {
            return ParsingInformation.success(matcher.start(), matcher.end());
        }
        return ParsingInformation.failure();
    }

    private ParsingInformation findWithFiniteAutomata(final int index) {
        for (int start = index; start < string.length(); start++) {
            for (int end = string.length(); end > start; end--) {
                final String sequenceToMatch = string.substring(start, end);
                if (matchedFiniteAutomaton(sequenceToMatch)) {
                    return ParsingInformation.success(start, end);
                }
            }
        }
        return ParsingInformation.failure();
    }

    private static final record ParsingInformation(int start, int end, boolean found) {

        private static ParsingInformation success(int start, int end) {
            return new ParsingInformation(start, end, true);
        }

        private static ParsingInformation failure() {
            return new ParsingInformation(-1, -1, false);
        }

        private boolean isSuccess() {
            return found;
        }
    }

    private boolean matchedFiniteAutomaton(final String sequenceToMatch) {
        for (final FiniteAutomaton finiteAutomaton : finiteAutomata) {
            if (FiniteAutomatonExecutorImpl.instance().apply(finiteAutomaton, sequenceToMatch)) {
                return true;
            }
        }
        return false;
    }

    private boolean setSuccess(final int start, final int end) {
        this.start = start;
        this.end = end;
        this.succeeded = true;
        return true;
    }

    private boolean setFailure() {
        this.succeeded = false;
        return false;
    }
}
