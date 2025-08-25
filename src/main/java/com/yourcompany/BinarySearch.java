package com.yourcompany;

import java.util.List;
import java.util.ArrayList;

public class BinarySearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int low = 0;
        int high = list.size() - 1;

        if (list instanceof ArrayList) {
            while (low <= high) {
                int mid = low + (high - low) / 2;
                counter++;
                int cmp = list.get(mid).compareTo(key);

                if (cmp == 0) {
                    long endTime = System.nanoTime();
                    return new SearchResult<>(list.get(mid), mid, counter, endTime - startTime);
                } else if (cmp < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        long endTime = System.nanoTime();
        return new SearchResult<>(null, -1, counter, endTime - startTime);
    }
}