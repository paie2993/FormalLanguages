package org.compilers.scanner.analyser.regex;

import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public final class RegexUtil {

    public static Pattern patternOf(final String... rules) {
        return Pattern.compile(joinRegex(rules));
    }

    public static String joinRegex(final Set<String> tokens) {
        final StringJoiner joiner = regexJoiner();
        tokens.forEach(joiner::add);
        return joiner.toString();
    }

    public static String joinRegex(String... rules) {
        final StringJoiner joiner = regexJoiner();
        Arrays.stream(rules).forEach(joiner::add);
        return joiner.toString();
    }

    public static StringJoiner regexJoiner() {
        return new StringJoiner("|");
    }
}
