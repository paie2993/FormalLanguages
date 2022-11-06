package org.compilers.analyser;

import org.compilers.internalForm.ProgramInternalForm;
import org.compilers.symboltable.HashSymbolTable;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Analyser {

    private static final String symbolFile = "src/main/java/files/output/symbol.txt";
    private static final String internalFormFile = "src/main/java/files/output/internal.txt";

    //will try to detect class 3 patterns
    private static Consumer<String> getSecondaryFallback(final Bundle bundle, final AtomicInteger lineNumber) {
        return secondarySequence -> processSequence(
                secondarySequence,
                Rules.secondarySeparatorsPattern,
                erroneousSequence -> signalLexicalError(erroneousSequence, lineNumber.get()),
                bundle
        );
    }

    // will try to detect class 2 patterns
    private static Consumer<String> getPrimaryFallback(final Bundle bundle, final AtomicInteger lineNumber) {
        return primarySequence -> processSequence(
                primarySequence,
                Rules.primarySeparatorsPattern,
                getSecondaryFallback(bundle, lineNumber),
                bundle
        );
    }

    public static void processProgram(final String filename) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(filename));
        final AtomicInteger lineNumber = new AtomicInteger(0);
        final Bundle bundle = new Bundle(new HashSymbolTable(), new ProgramInternalForm());

        final Consumer<String> primaryFallback = getPrimaryFallback(bundle, lineNumber);

        while (reader.ready()) {
            final String rawLine = reader.readLine();
            processLine(rawLine, lineNumber, primaryFallback, bundle);
        }
        reader.close();

        bundle.printToFiles(symbolFile, internalFormFile);
    }

    private static void processLine(
            final String line,
            final AtomicInteger lineNumber,
            final Consumer<String> fallback,
            final Bundle bundle
    ) {
        lineNumber.getAndIncrement();
        final String trimmedLine = line.trim();
        processSequence(trimmedLine, Rules.tokenPattern, fallback, bundle);
    }

    private static void processSequence(
            final String sequence,
            final Pattern pattern,
            final Consumer<String> fallback,
            final Bundle bundle
    ) {
        final Matcher matcher = pattern.matcher(sequence);

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


    private static void signalLexicalError(String faultySequence, int lineNumber) {
        throw new RuntimeException("Lexical error: " + faultySequence + " at line " + lineNumber);
    }


    static boolean isSeparator(final String token) {
        return Rules.pureSeparatorsPattern.matcher(token).matches();
    }

    static boolean isOperator(final String token) {
        return Rules.priorityOperatorsPattern.matcher(token).matches() ||
                Rules.secondaryOperatorsPattern.matcher(token).matches();
    }

    static boolean isKeyword(final String token) {
        return Rules.keywordsPattern.matcher(token).matches();
    }

    static boolean isIdentifier(final String token) {
        return Rules.identifiersPattern.matcher(token).matches();
    }

    static boolean isConstant(final String token) {
        return Rules.constantsPattern.matcher(token).matches();
    }
}
