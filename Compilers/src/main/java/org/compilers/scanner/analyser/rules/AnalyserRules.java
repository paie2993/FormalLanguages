package org.compilers.scanner.analyser.rules;

import org.compilers.configuration.*;
import org.compilers.finite_automaton.automaton_io.automaton_reader.FiniteAutomatonReader;
import org.compilers.finite_automaton.automaton_io.automaton_reader.FiniteAutomatonReaderImpl;
import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;
import org.compilers.scanner.analyser.matcher.TokenMatcher;
import org.compilers.scanner.analyser.matcher.TokenMatcherImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static org.compilers.scanner.analyser.regex.RegexUtil.joinRegex;

public final class AnalyserRules {

    //class 1
    public static final String keywordsRegex; // reserved words
    public static final String stringRegex = "\"[^\"]*\"";
    public static final String characterRegex = "'[^']'";
    public static final String booleanRegex = "true|false";

    //class 2
    public static final String unregistrableSeparatorsRegex = " +"; // one or more spaces, should not be registered in the pif
    public static final String registrableSeparatorsRegex; // separators such as: ( ) [ ] { } :, which should be registered in pif
    public static final String primaryOperatorsRegex; // operators such as: <= >= == !=, which are first searched for in a line of code

    //class 3
    // operators such as: +, -, =, *, /, &&, ||, %, <, >, which are searched for only after the compound operators (
    // priorityOperators) are searched for
    public static final String secondaryOperatorsRegex;

    // initialize the tokens that must be read from Tokens file
    static {
        try (final BufferedReader reader = new BufferedReader(new FileReader(Configuration.TOKENS_FILE))) {
            registrableSeparatorsRegex = joinRegex(readTokenCategory(reader));
            primaryOperatorsRegex = joinRegex(readTokenCategory(reader));
            secondaryOperatorsRegex = joinRegex(readTokenCategory(reader));
            keywordsRegex = joinRegex(readTokenCategory(reader));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // define the patterns for regex matching
    public static final FiniteAutomaton identifiersFiniteAutomaton;
    public static final FiniteAutomaton integersFiniteAutomaton;

    static {
        final FiniteAutomatonReader reader = FiniteAutomatonReaderImpl.instance();
        try {
            identifiersFiniteAutomaton = reader.read(Configuration.IDENTIFIER_FINITE_AUTOMATON_FILE, Configuration.FINITE_AUTOMATON_CLASS);
            integersFiniteAutomaton = reader.read(Configuration.INTEGER_FINITE_AUTOMATON_FILE, Configuration.FINITE_AUTOMATON_CLASS);
        } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static TokenMatcher identifierMatcher(final String sequenceToMatch) {
        return new TokenMatcherImpl(sequenceToMatch, identifiersFiniteAutomaton);
    }

    public static TokenMatcher constantsMatcher(final String sequenceToMatch) {
        return new TokenMatcherImpl(sequenceToMatch, Set.of(booleanRegex, characterRegex, stringRegex), Set.of(integersFiniteAutomaton));
    }

    public static TokenMatcher keywordsMatcher(final String sequenceToMatch) {
        return new TokenMatcherImpl(sequenceToMatch, keywordsRegex);
    }

    public static TokenMatcher separatorsMatcher(final String sequenceToMatch) {
        return new TokenMatcherImpl(sequenceToMatch, unregistrableSeparatorsRegex, registrableSeparatorsRegex);
    }

    public static TokenMatcher operatorsMatcher(final String sequenceToMatch) {
        return new TokenMatcherImpl(sequenceToMatch, primaryOperatorsRegex, secondaryOperatorsRegex);
    }


    public static TokenMatcher firstClassTokensMatcher(final String sequenceToMatch) {
        return new TokenMatcherImpl(
                sequenceToMatch,
                Set.of(stringRegex, characterRegex, booleanRegex, keywordsRegex),
                Set.of(identifiersFiniteAutomaton, integersFiniteAutomaton)
        );
    }

    public static TokenMatcher secondClassTokensMatcher(final String sequenceToMatch) {
        return new TokenMatcherImpl(sequenceToMatch, unregistrableSeparatorsRegex, registrableSeparatorsRegex, primaryOperatorsRegex);
    }

    public static TokenMatcher thirdClassTokensMatcher(final String sequenceToMatch) {
        return new TokenMatcherImpl(sequenceToMatch, secondaryOperatorsRegex);
    }


    //reads from file until empty line or end of file
    private static Set<String> readTokenCategory(final BufferedReader reader) throws IOException {
        final Set<String> tokens = new HashSet<>();

        String token = readToken(reader);
        while (!token.isEmpty()) {
            tokens.add(token);
            token = readToken(reader);
        }

        return tokens;
    }

    private static String readToken(final BufferedReader reader) throws IOException {
        return reader.readLine().trim();
    }
}
