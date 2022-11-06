package org.compilers.pair;

public final record Pair<I extends Comparable<I>, V>(I index, V id) implements Comparable<Pair<I, V>> {

    @Override
    public int compareTo(Pair<I, V> o) {
        return this.index.compareTo(o.index);
    }
}
