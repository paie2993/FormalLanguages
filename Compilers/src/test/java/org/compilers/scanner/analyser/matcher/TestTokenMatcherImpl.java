package org.compilers.scanner.analyser.matcher;

import org.compilers.configuration.Configuration;
import org.compilers.finite_automaton.automaton_io.automaton_reader.FiniteAutomatonReaderImpl;
import org.compilers.finite_automaton.finite_automaton.FiniteAutomaton;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

class TestTokenMatcherImpl {

    @Test
    void findVersionOne() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("_a", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionTwo() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("__a", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionThree() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("___a", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionFour() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("___aBcD", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionFive() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("abc___aBcD", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionSix() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("a*_*a", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionSeven() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("abc", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionEight() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertFalse(found);
    }

    @Test
    void findVersionNine() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("123", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertFalse(found);
    }

    @Test
    void findVersionTen() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("_abc123", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionEleven() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("_123", identifierFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertFalse(found);
    }

    @Test
    void findVersionTwelve() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("abc", identifierFiniteAutomaton());

        final boolean found = matcher.find(1);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionThirteen() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("abc", identifierFiniteAutomaton());

        final boolean found = matcher.find(2);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionFourteen() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("abc", identifierFiniteAutomaton());

        final boolean found = matcher.find(3);

        Assertions.assertFalse(found);
    }

    @Test
    void findVersionFifteen() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("0", integerFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionSixteen() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("+0", integerFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionSeventeen() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("-0", integerFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionEighteen() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("-0123", integerFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionNineteen() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("-0123", integerFiniteAutomaton());

        final boolean found = matcher.find(1);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionTwenty() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("-0123", integerFiniteAutomaton());

        final boolean found = matcher.find(2);

        Assertions.assertTrue(found);
    }

    @Test
    void findVersionTwentyOne() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("abc", integerFiniteAutomaton());

        final boolean found = matcher.find(0);

        Assertions.assertFalse(found);
    }

    @Test
    void startVersionOne() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("_a", identifierFiniteAutomaton());

        matcher.find(0);
        final int start = matcher.start();

        Assertions.assertEquals(0, start);
    }

    @Test
    void startVersionTwo() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("123_abc", identifierFiniteAutomaton());

        matcher.find(0);
        final int start = matcher.start();

        Assertions.assertEquals(3, start);
    }

    @Test
    void startVersionThree() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("123_abc", identifierFiniteAutomaton());

        matcher.find(1);
        final int start = matcher.start();

        Assertions.assertEquals(3, start);
    }

    @Test
    void startVersionFour() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("123_abc", identifierFiniteAutomaton());

        matcher.find(2);
        final int start = matcher.start();

        Assertions.assertEquals(3, start);
    }

    @Test
    void startVersionFive() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("123_abc", identifierFiniteAutomaton());

        matcher.find(3);
        final int start = matcher.start();

        Assertions.assertEquals(3, start);
    }

    @Test
    void startVersionSix() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("123_abc", identifierFiniteAutomaton());

        matcher.find(4);
        final int start = matcher.start();

        Assertions.assertNotEquals(3, start);
    }

    @Test
    void startVersionSeven() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("123_abc", identifierFiniteAutomaton());

        matcher.find(4);
        final int start = matcher.start();

        Assertions.assertEquals(4, start);
    }

    @Test
    void endVersionOne() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("_abc", identifierFiniteAutomaton());

        matcher.find(0);
        final int end = matcher.end();

        Assertions.assertEquals(4, end);
    }

    @Test
    void endVersionTwo() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("_abc", identifierFiniteAutomaton());

        matcher.find(1);
        final int end = matcher.end();

        Assertions.assertEquals(4, end);
    }

    @Test
    void endVersionThree() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final TokenMatcher matcher = new TokenMatcherImpl("1_a*", identifierFiniteAutomaton());

        matcher.find(0);
        final int end = matcher.end();

        Assertions.assertEquals(3, end);
    }

    private static FiniteAutomaton identifierFiniteAutomaton()
            throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return FiniteAutomatonReaderImpl.instance().read(Configuration.IDENTIFIER_FINITE_AUTOMATON_FILE, Configuration.FINITE_AUTOMATON_CLASS);
    }

    private static FiniteAutomaton integerFiniteAutomaton()
            throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return FiniteAutomatonReaderImpl.instance().read(Configuration.INTEGER_FINITE_AUTOMATON_FILE, Configuration.FINITE_AUTOMATON_CLASS);
    }
}
