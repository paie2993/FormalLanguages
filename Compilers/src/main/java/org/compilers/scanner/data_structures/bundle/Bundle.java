package org.compilers.scanner.data_structures.bundle;

import org.compilers.scanner.analyser.Analyser;
import org.compilers.scanner.analyser.rules.AnalyserRules;
import org.compilers.scanner.data_structures.internalForm.ProgramInternalForm;
import org.compilers.scanner.data_structures.symboltable.SymbolTable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final record Bundle(SymbolTable symbolTable, ProgramInternalForm programInternalForm) {

    public void add(final String token) {
        if (Analyser.isSeparator(token) || Analyser.isOperator(token) || Analyser.isKeyword(token)) {
            if (!token.matches(AnalyserRules.unregistrableSeparatorsRegex)) {
                programInternalForm.add(token, -1);
            }
        } else if (Analyser.isConstant(token)) {
            int symbolTableIndex = symbolTable.put(token);
            programInternalForm.add("constant", symbolTableIndex);
        } else if (Analyser.isIdentifier(token)) {
            int symbolTableIndex = symbolTable.put(token);
            programInternalForm.add("identifier", symbolTableIndex);
        } else {
            throw new RuntimeException("Token could not be classified: " + token);
        }
    }

    public void printToFiles(
            final String symbolFile,
            final String internalFormFile
    ) throws IOException {
        printToFile(symbolTable, symbolFile);
        printToFile(programInternalForm, internalFormFile);
    }

    private static void printToFile(final Object obj, final String fileName) throws IOException {
        final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(obj.toString());
        writer.close();
    }
}