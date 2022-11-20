package org.compilers.scanner.analyser;

import org.compilers.configuration.Configuration;
import org.compilers.scanner.analyser.matcher.TokenMatcher;
import org.compilers.scanner.analyser.rules.AnalyserRules;
import org.compilers.scanner.data_structures.bundle.Bundle;
import org.compilers.scanner.data_structures.internalForm.ProgramInternalForm;
import org.compilers.scanner.data_structures.symboltable.HashSymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Analyser {

    // will try to detect class 1 patterns
    private static Consumer<String> primarySequencesProcessor(final Bundle bundle, final AtomicInteger lineNumber) {
        return primarySequence -> processSequence(
                primarySequence,
                AnalyserRules::firstClassTokensMatcher,
                secondarySequencesProcessor(bundle, lineNumber),
                bundle
        );
    }

    // will try to detect class 2 patterns
    private static Consumer<String> secondarySequencesProcessor(final Bundle bundle, final AtomicInteger lineNumber) {
        return secondarySequence -> processSequence(
                secondarySequence,
                AnalyserRules::secondClassTokensMatcher,
                ternarySequencesProcessor(bundle, lineNumber),
                bundle
        );
    }

    //will try to detect class 3 patterns
    private static Consumer<String> ternarySequencesProcessor(final Bundle bundle, final AtomicInteger lineNumber) {
        return ternarySequence -> processSequence(
                ternarySequence,
                AnalyserRules::thirdClassTokensMatcher,
                erroneousSequence -> signalLexicalError(erroneousSequence, lineNumber.get()),
                bundle
        );
    }

    public static void processProgram(final String fileName) throws IOException {
        final Bundle bundle = new Bundle(new HashSymbolTable(), new ProgramInternalForm());
        final AtomicInteger lineNumber = new AtomicInteger(1);

        final Consumer<String> primaryProcessor = primarySequencesProcessor(bundle, lineNumber);

        try (final BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.ready()) {
                final String line = readLine(reader);
                primaryProcessor.accept(line);
                lineNumber.getAndIncrement();
            }
        }

        bundle.printToFiles(Configuration.SYMBOL_TABLE_OUTPUT_FILE, Configuration.PIF_OUTPUT_FILE);
    }

    private static String readLine(final BufferedReader reader) throws IOException {
        return reader.readLine().trim();
    }

    private static void processSequence(
            final String sequence,
            final Function<String, TokenMatcher> tokenMatcherFactory,
            final Consumer<String> fallback,
            final Bundle bundle
    ) {
        final TokenMatcher matcher = tokenMatcherFactory.apply(sequence);

        int parsingIndex = 0;
        while (parsingIndex <= sequence.length() - 1) {

            boolean found = matcher.find(parsingIndex);
            if (!found) {
                String endOfLine = sequence.substring(parsingIndex);
                fallback.accept(endOfLine);
                return;
            }

            int matchStart = matcher.start();

            if (parsingIndex != matchStart) {
                String secondaryTokensSequence = sequence.substring(parsingIndex, matchStart);
                fallback.accept(secondaryTokensSequence);
            }

            int end = matcher.end();
            String token = sequence.substring(matchStart, end);
            bundle.add(token);

            parsingIndex = end;
        }
    }


    private static void signalLexicalError(final String faultySequence, final int lineNumber) {
        throw new RuntimeException("Lexical error: " + faultySequence + " at line " + lineNumber);
    }


    public static boolean isSeparator(final String token) {
        return AnalyserRules.separatorsMatcher(token).matches();
    }

    public static boolean isOperator(final String token) {
        return AnalyserRules.operatorsMatcher(token).matches();
    }

    public static boolean isKeyword(final String token) {
        return AnalyserRules.keywordsMatcher(token).matches();
    }

    public static boolean isIdentifier(final String token) {
        return AnalyserRules.identifierMatcher(token).matches();
    }

    public static boolean isConstant(final String token) {
        return AnalyserRules.constantsMatcher(token).matches();
    }
}
