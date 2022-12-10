package org.compilers.grammar.model.production;

import org.compilers.grammar.model.vocab.Vocab;

import java.util.List;
import java.util.Objects;

public final class Production {


    private static final String sidesSeparator = " -> ";

    private final List<Vocab> left;

    private final List<Vocab> right;

    public Production(final List<Vocab> left, final List<Vocab> right) {
        validateSide(left);
        validateSide(right);

        this.left = left;
        this.right = right;
    }

    public List<Vocab> left() {
        return left;
    }

    public List<Vocab> right() {
        return right;
    }

    @Override
    public String toString() {
        return prepareListForPrinting(left) + sidesSeparator + prepareListForPrinting(right);
    }

    private static String prepareListForPrinting(final List<Vocab> list) {
        final var builder = new StringBuilder();
        list.forEach(builder::append);
        return builder.toString();
    }

    private static void validateSide(final List<Vocab> side) {
        Objects.requireNonNull(side);
        if (side.isEmpty()) {
            throw new IllegalArgumentException("Side of production can't be empty");
        }
    }
}
