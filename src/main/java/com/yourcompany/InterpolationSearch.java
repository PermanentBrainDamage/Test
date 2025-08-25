package com.yourcompany;

import java.util.List;
import java.util.ArrayList;

public class InterpolationSearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int low = 0;
        int high = list.size() - 1;

        if (list instanceof ArrayList) {
            while (low <= high && key.compareTo(list.get(low)) >= 0 && key.compareTo(list.get(high)) <= 0) {
                counter++;

                if (list.get(high).compareTo(list.get(low)) == 0) {
                    if (key.compareTo(list.get(low)) == 0) {
                        counter++;
                        long endTime = System.nanoTime();
                        return new SearchResult<>(list.get(low), low, counter, endTime - startTime);
                    } else {
                        long endTime = System.nanoTime();
                        return new SearchResult<>(null, -1, counter, endTime - startTime);
                    }
                }

                double pos = (double) (high - low) / (list.get(high).compareTo(list.get(low))) * (key.compareTo(list.get(low)));
                int middle = low + (int) pos;

                counter++;
                if (list.get(middle).compareTo(key) == 0) {
                    long endTime = System.nanoTime();
                    return new SearchResult<>(list.get(middle), middle, counter, endTime - startTime);
                }

                if (list.get(middle).compareTo(key) < 0) {
                    low = middle + 1;
                } else {
                    high = middle - 1;
                }
            }
        }
        long endTime = System.nanoTime();
        return new SearchResult<>(null, -1, counter, endTime - startTime);
    }
}