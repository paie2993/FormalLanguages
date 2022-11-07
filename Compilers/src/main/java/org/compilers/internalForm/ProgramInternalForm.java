package org.compilers.internalForm;

import org.compilers.pair.Pair;

import java.util.ArrayList;
import java.util.List;

public final class ProgramInternalForm {

    private final List<Pair<String, Integer>> pif = new ArrayList<>();

    public void add(final String token, final int index) {
        pif.add(new Pair<>(token, index));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        final String format = "\t%-20s\t|\t%-10s\t\n";
        builder.append(String.format(format, "token", "symbol table index"));
        builder.append("-----------------------------------------------\n");
        pif.forEach(pair -> builder.append(String.format(format, pair.first(), pair.second())));
        return builder.toString();
    }
}
