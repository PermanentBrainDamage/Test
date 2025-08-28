package com.yourcompany;

import java.util.List;
import java.util.ListIterator;

public class JumpSearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int n = list.size();
        int step = (int) Math.floor(Math.sqrt(n));
        int prev = 0;

        // Iterator to handle LinkedList efficiently
        ListIterator<T> it = list.listIterator();

        // Find block where key may exist
        while (prev < n) {
            int blockEnd = Math.min(prev + step, n);
            T lastInBlock = null;

            // Move iterator to blockEnd
            while (it.nextIndex() < blockEnd) {
                lastInBlock = it.next();
            }
            counter++;

            if (key.compareTo(lastInBlock) <= 0) {
                // Linear scan in block
                for (int i = prev; i < blockEnd; i++) {
                    counter++;
                    T current = list.get(i);
                    if (current.equals(key)) {
                        long endTime = System.nanoTime();
                        return new SearchResult<>("Jump Search", current, i, counter, endTime - startTime);
                    }
                }
                long endTime = System.nanoTime();
                return new SearchResult<>("Jump Search", null, -1, counter, endTime - startTime);
            }

            prev += step;
        }

        long endTime = System.nanoTime();
        return new SearchResult<>("Jump Search", null, -1, counter, endTime - startTime);
    }
}
