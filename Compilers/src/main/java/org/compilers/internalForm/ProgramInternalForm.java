package org.compilers.internalForm;

import org.compilers.pair.Pair;

import java.util.ArrayList;
import java.util.List;

public final class ProgramInternalForm {

    private final List<Pair<Integer, String>> pif = new ArrayList<>();

    public void add(final int index, final String token) {
        pif.add(new Pair<>(index, token));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        final String format = "\t%-10s\t|\t%-20s\t\n";
        builder.append(String.format(format, "id", "index"));
        builder.append("-----------------------------------------------\n");
        pif.forEach(pair ->
                builder.append(String.format(format, pair.index(), pair.id()))
        );
        return builder.toString();
    }
}
