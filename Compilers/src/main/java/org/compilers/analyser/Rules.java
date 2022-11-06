package org.compilers.analyser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Pattern;

final class Rules {

    private static final String tokensFileName = "src/main/java/files/static/Tokens.txt";

    //class 1
    static final String keywordsRegex;

    //class 2
    static final String unregistrableSeparatorsRegex = " +";
    static final String registrableSeparatorsRegex;
    static final String priorityOperatorsRegex;

    //class 3
    static final String secondaryOperatorsRegex;

    static {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(tokensFileName));
            final Set<String> separators = readTokenCategory(reader);
            final Set<String> priorityOperators = readTokenCategory(reader);
            final Set<String> secondaryOperators = readTokenCategory(reader);
            final Set<String> keywords = readTokenCategory(reader);
            reader.close();

            registrableSeparatorsRegex = buildRegexFromSet(separators);
            priorityOperatorsRegex = buildRegexFromSet(priorityOperators);
            secondaryOperatorsRegex = buildRegexFromSet(secondaryOperators);
            keywordsRegex = buildRegexFromSet(keywords);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //class 1
    static final String identifiersRegex = "_?[a-zA-Z]+[a-zA-Z0-9]*";
    static final String stringRegex = "\"[^\"]*\"";
    static final String integerRegex = "0|[-+]?[1-9][0-9]*";
    static final String characterRegex = "'[^']'";
    static final String booleanRegex = "true|false";

    static final Pattern identifiersPattern = Pattern.compile(identifiersRegex);
    static final Pattern constantsPattern = Pattern.compile(buildRegexFromStrings(integerRegex, booleanRegex, characterRegex, stringRegex));


    static final Pattern pureSeparatorsPattern = Pattern.compile(buildRegexFromStrings(unregistrableSeparatorsRegex, registrableSeparatorsRegex));
    static final Pattern priorityOperatorsPattern = Pattern.compile(priorityOperatorsRegex);
    static final Pattern secondaryOperatorsPattern = Pattern.compile(secondaryOperatorsRegex);
    static final Pattern keywordsPattern = Pattern.compile(keywordsRegex);

    static final Pattern primarySeparatorsPattern;
    static final Pattern secondarySeparatorsPattern;
    static final Pattern tokenPattern;

    static {
        final String separatorTokenRule =
                buildRegexFromStrings(unregistrableSeparatorsRegex, registrableSeparatorsRegex, priorityOperatorsRegex);
        final String significantTokenRule =
                buildRegexFromStrings(stringRegex, identifiersRegex, integerRegex, characterRegex, booleanRegex, keywordsRegex);

        primarySeparatorsPattern = Pattern.compile(separatorTokenRule);
        secondarySeparatorsPattern = secondaryOperatorsPattern;
        tokenPattern = Pattern.compile(significantTokenRule);
    }

    //reads from file until empty line or end of file
    private static Set<String> readTokenCategory(final BufferedReader reader) throws IOException {
        final Set<String> tokens = new HashSet<>();
        boolean done = false;
        while (reader.ready() && !done) {
            final String token = reader.readLine().trim();
            if (token.isEmpty()) {
                done = true;
            } else {
                tokens.add(token);
            }
        }
        return tokens;
    }


    private static String buildRegexFromSet(final Set<String> tokens) {
        final StringJoiner joiner = getRegexJoiner();
        tokens.forEach(joiner::add);
        return joiner.toString();
    }

    private static String buildRegexFromStrings(String... rules) {
        StringJoiner joiner = getRegexJoiner();
        for (String rule : rules) {
            joiner.add(rule);
        }
        return joiner.toString();
    }

    private static StringJoiner getRegexJoiner() {
        return new StringJoiner("|");
    }
}
