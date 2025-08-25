package com.yourcompany;

import java.util.List;
import java.util.ArrayList;

public class JumpSearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int n = list.size();

        if (list instanceof ArrayList) {
            int step = (int) Math.floor(Math.sqrt(n));
            int prev = 0;

            while (list.get(Math.min(step, n) - 1).compareTo(key) < 0) {
                counter++;
                prev = step;
                step += (int) Math.floor(Math.sqrt(n));
                if (prev >= n) {
                    long endTime = System.nanoTime();
                    return new SearchResult<>(null, -1, counter, endTime - startTime);
                }
            }

            while (list.get(prev).compareTo(key) < 0) {
                counter++;
                prev++;
                if (prev == Math.min(step, n)) {
                    long endTime = System.nanoTime();
                    return new SearchResult<>(null, -1, counter, endTime - startTime);
                }
            }

            counter++;
            if (list.get(prev).compareTo(key) == 0) {
                long endTime = System.nanoTime();
                return new SearchResult<>(list.get(prev), prev, counter, endTime - startTime);
            }
        }
        long endTime = System.nanoTime();
        return new SearchResult<>(null, -1, counter, endTime - startTime);
    }
}