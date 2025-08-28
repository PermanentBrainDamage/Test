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
                return new SearchResult<>(
                        "Linear Search",   // algorithm name
                        list.get(i),      // found item
                        i,                // index
                        counter,          // number of comparisons
                        endTime - startTime // elapsed time
                );
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult<>(
                "Linear Search",  // algorithm name
                null,             // no item found
                -1,               // invalid index
                counter,          // total comparisons
                endTime - startTime
        );
    }
}
