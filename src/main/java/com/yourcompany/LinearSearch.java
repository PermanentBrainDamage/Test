package com.yourcompany;

import java.util.List;


public class LinearSearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {
    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int index = 0;


        for (T element : list) {
            counter++;
            if (element.compareTo(key) == 0) {
                long endTime = System.nanoTime();
                return new SearchResult<>(
                        "Linear Search",
                        element,
                        index,
                        counter,
                        endTime - startTime
                );
            }
            index++;
        }

        long endTime = System.nanoTime();
        SearchResult<T> linearSearch = new SearchResult<>(
                "Linear Search",
                null,
                -1,
                counter,
                endTime - startTime
        );
        return linearSearch;
    }
}