package com.yourcompany;

import java.util.List;

public class LinearSearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        for (int i = 0; i < list.size(); i++) {
            counter++;
            if (list.get(i).compareTo(key) == 0) {
                long endTime = System.nanoTime();
                return new SearchResult<>(list.get(i), i, counter, endTime - startTime);
            }
        }
        long endTime = System.nanoTime();
        return new SearchResult<>(null, -1, counter, endTime - startTime);
    }
}