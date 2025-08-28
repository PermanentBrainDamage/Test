package com.yourcompany;

import java.util.List;
import java.util.RandomAccess;
import java.util.Iterator;

public class InterpolationSearch<T extends Comparable<T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int low = 0;
        int high = list.size() - 1;

        // Determine if list supports random access
        boolean isRandomAccess = list instanceof RandomAccess;

        while (low <= high) {
            counter++;

            // Prevent division by zero
            T lowVal = get(list, low, isRandomAccess);
            T highVal = get(list, high, isRandomAccess);
            int range = highVal.compareTo(lowVal);
            if (range == 0) {
                if (lowVal.equals(key)) {
                    long endTime = System.nanoTime();
                    return new SearchResult<>("InterpolationSearch", lowVal, low, counter, endTime - startTime);
                }
                break;
            }

            // Estimate position
            int pos = low + (int) ((double)(high - low) / range * lowVal.compareTo(key) * -1);
            pos = Math.max(low, Math.min(pos, high)); // clamp pos within bounds

            T posVal = get(list, pos, isRandomAccess);
            counter++;

            if (posVal.equals(key)) {
                long endTime = System.nanoTime();
                return new SearchResult<>("InterpolationSearch", posVal, pos, counter, endTime - startTime);
            } else if (posVal.compareTo(key) < 0) {
                low = pos + 1;
            } else {
                high = pos - 1;
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult<>("InterpolationSearch", null, -1, counter, endTime - startTime);
    }

    // Helper to get element safely depending on RandomAccess
    private T get(List<T> list, int index, boolean isRandomAccess) {
        if (isRandomAccess) {
            return list.get(index);
        } else {
            // Sequential access for LinkedList
            Iterator<T> iter = list.listIterator(index);
            return iter.next();
        }
    }
}
