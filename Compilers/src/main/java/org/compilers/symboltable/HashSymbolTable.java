package org.compilers.symboltable;

import org.compilers.pair.Pair;

import java.util.*;

public final class HashSymbolTable implements SymbolTable {

    private static final int initialCapacity = 8;
    private static final int initialMaximumCapacity = 12;

    private int currentCapacity = initialCapacity;
    private int currentMaximumCapacity = initialMaximumCapacity;
    private int currentIndex = 0;

    private LinkedListWrapper[] hashTable = initializeHashTable(currentCapacity);

    @Override
    public int put(String id) {
        Objects.requireNonNull(id);
        var pairWithId = findPairWithId(id);
        if (pairWithId.isPresent()) {
            return pairWithId.get().index();
        } else {
            resizeHashTableIfNeeded();
            return registerId(id);
        }
    }

    static int tableHashCode(String id, int capacity) {
        return Math.abs(id.hashCode() % capacity);
    }

    Optional<Pair<Integer, String>> findPairWithId(String id) {
        var list = findIdList(id);
        return list.stream().filter(pair -> pair.id().equals(id)).findFirst();
    }

    LinkedList<Pair<Integer, String>> findIdList(String id) {
        int hashCode = tableHashCode(id, currentCapacity);
        return hashTable[hashCode].list;
    }

    int registerId(String id) {
        var list = findIdList(id);
        list.add(new Pair<>(currentIndex, id));
        return currentIndex++;
    }

    private void resizeHashTableIfNeeded() {
        if (currentIndex == currentMaximumCapacity) {
            resizeHashTable();
        }
    }

    private void resizeHashTable() {
        resizeCapacities();
        hashTable = createNewHashTable();
    }

    private void resizeCapacities() {
        currentCapacity = currentCapacity * 2;
        currentMaximumCapacity = currentMaximumCapacity * 2;
    }

    private LinkedListWrapper[] createNewHashTable() {
        var newHashTable = initializeHashTable(currentCapacity);
        for (var list : hashTable) {
            for (var pair : list.list) {
                int idHashCode = tableHashCode(pair.id(), currentCapacity);
                newHashTable[idHashCode].list.add(pair);
            }
        }
        return newHashTable;
    }

    private static final record LinkedListWrapper(LinkedList<Pair<Integer, String>> list) {
    }

    private static LinkedListWrapper[] initializeHashTable(int capacity) {
        LinkedListWrapper[] list = new LinkedListWrapper[capacity];
        for (int i = 0; i < capacity; i++) {
            list[i] = new LinkedListWrapper(new LinkedList<>());
        }
        return list;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        final String format = "\t%-10s\t|\t%-20s\t\n";
        builder.append(String.format(format, "id", "index"));
        builder.append("----------------------------------------------\n");
        final List<Pair<Integer, String>> orderedPairs = prepareForPrint();
        orderedPairs.forEach(pair ->
                builder.append(String.format(format, pair.index(), pair.id()))
        );
        return builder.toString();
    }

    private List<Pair<Integer, String>> prepareForPrint() {
        final Set<Pair<Integer, String>> orderedSet = new TreeSet<>();
        for (LinkedListWrapper wrapper : hashTable) {
            orderedSet.addAll(wrapper.list);
        }
        return orderedSet.stream().toList();
    }
}
