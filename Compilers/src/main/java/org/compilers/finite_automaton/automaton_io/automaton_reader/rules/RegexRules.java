package org.compilers.finite_automaton.automaton_io.automaton_reader.rules;

/**
 * Specifies the rules for reading a final automaton
 */
public final class RegexRules {

    public static final class FileRules {
        public static final String batchSeparator = "";
    }

    /**
     * Specifies how the states and the alphabet should look
     */
    public static final class VocabsRules {
        public static final String stateRule = "[a-zA-Z0-9]+";
        public static final String symbolRule = "[_\\+\\-a-zA-Z0-9]";
    }

    /**
     * Specifies how the transition should look (syntax)
     */
    public static abstract class TransitionRules {
        // the left-hand side of the transition rule
        public static final String transitionPrefix = "d\\(";
        public static final String argumentsSeparator = ", ";
        public static final String transitionSuffix = "\\)";

        // separated the left-hand side from the right-hand side
        public static final String transitionSidesSeparator = " = ";

        // the right-hand side of the transition rule
        public static final String resultStatesPrefix = "\\{ ";
        public static final String resultStatesSeparator = " "; // separator of the result states of the transition
        public static final String resultStatesSuffix = " \\}";

        public static final String resultStatesRule = buildResultStatesRule();

        public static final String transitionRule = buildCompleteRule();


        // The regex looks like: One single state, or several states separated by their separator
        // [a-zA-Z0-9]+(( [a-zA-Z0-9]+)+)?
        private static String buildResultStatesRule() {
            return VocabsRules.stateRule + "((" + resultStatesSeparator + VocabsRules.stateRule + ")+)?";
        }

        // The result looks so:  ^d\([a-zA-Z0-9]+, [a-zA-Z0-9]\) = \{ [a-zA-Z0-9]+(( [a-zA-Z0-9]+)+)? \}$
        private static String buildCompleteRule() {
            return "^" +
                    TransitionRules.transitionPrefix +
                    VocabsRules.stateRule +
                    TransitionRules.argumentsSeparator +
                    VocabsRules.symbolRule +
                    TransitionRules.transitionSuffix +
                    TransitionRules.transitionSidesSeparator +
                    TransitionRules.resultStatesPrefix +
                    TransitionRules.resultStatesRule +
                    TransitionRules.resultStatesSuffix +
                    "$";
        }
    }

    public static String toSimpleString(final String regex) {
        return regex.replaceAll("\\\\", "");
    }
}
