package com.yourcompany;

import java.util.List;
import java.util.ListIterator;

public class BinarySearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;

        // Use efficient random access if possible
        if (list instanceof java.util.RandomAccess) {
            int low = 0;
            int high = list.size() - 1;

            while (low <= high) {
                int mid = low + (high - low) / 2;
                counter++;
                int cmp = list.get(mid).compareTo(key);

                if (cmp == 0) {
                    long endTime = System.nanoTime();
                    return new SearchResult<>("Binary Search", list.get(mid), mid, counter, endTime - startTime);
                } else if (cmp < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        } else {
            // Iterator-based binary search for LinkedList
            int size = list.size();
            int low = 0;
            int high = size - 1;

            while (low <= high) {
                int mid = low + (high - low) / 2;
                counter++;

                // Move iterator to mid
                ListIterator<T> it = list.listIterator(low);
                T midValue = null;
                for (int i = low; i <= mid; i++) {
                    midValue = it.next();
                }

                int cmp = midValue.compareTo(key);

                if (cmp == 0) {
                    long endTime = System.nanoTime();
                    return new SearchResult<>("Binary Search", midValue, mid, counter, endTime - startTime);
                } else if (cmp < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }

        long endTime = System.nanoTime();
        // Not found
        return new SearchResult<>("Binary Search", null, -1, counter, endTime - startTime);
    }
}
