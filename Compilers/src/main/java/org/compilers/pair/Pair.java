package org.compilers.pair;

public record Pair<I extends Comparable<I>, V extends Comparable<V>>(
        I first,
        V second
) implements Comparable<Pair<I, V>> {

    @Override
    public int compareTo(Pair<I, V> o) {
        return this.first.compareTo(o.first);
    }
}
