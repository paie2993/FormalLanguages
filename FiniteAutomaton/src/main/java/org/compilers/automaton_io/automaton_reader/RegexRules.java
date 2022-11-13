package org.compilers.automaton_io.automaton_reader;

public final class RegexRules {

    public static final class VocabsRules {
        public static final String stateRule = "[a-zA-Z0-9]+";
        public static final String symbolRule = "[a-zA-Z0-9]";
    }

    public static abstract class TransitionRules {
        public static final String transitionPrefix = "d\\(";
        public static final String argumentsSeparator = ", ";
        public static final String transitionSuffix = "\\)";

        public static final String transitionEquality = " = ";

        public static final String statesSetPrefix = "\\{ ";
        public static final String statesSetElementsSeparator = " ";
        public static final String statesSetSuffix = " \\}";

        public static final String resultStatesRule = buildResultStatesRule();

        public static final String completeRule = buildCompleteRule();


        // The regex looks like: "VocabsRule.state(( VocabsRule.state)+)?"
        private static String buildResultStatesRule() {
            return VocabsRules.stateRule + "((" + statesSetElementsSeparator + VocabsRules.stateRule + ")+)?";
        }

        private static String buildCompleteRule() {
            return "^" +
                    TransitionRules.transitionPrefix +
                    VocabsRules.stateRule +
                    TransitionRules.argumentsSeparator +
                    VocabsRules.symbolRule +
                    TransitionRules.transitionSuffix +
                    TransitionRules.transitionEquality +
                    TransitionRules.statesSetPrefix +
                    TransitionRules.resultStatesRule +
                    TransitionRules.statesSetSuffix +
                    "$";
        }
    }
}
