package org.compilers.analyser;

import org.compilers.internalForm.ProgramInternalForm;
import org.compilers.symboltable.SymbolTable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

final record Bundle(SymbolTable symbolTable, ProgramInternalForm programInternalForm) {

    void add(final String token) {
        if (Analyser.isSeparator(token) || Analyser.isOperator(token) || Analyser.isKeyword(token)) {
            if (!token.matches(Rules.unregistrableSeparatorsRegex)) {
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

    void printToFiles(
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