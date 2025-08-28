package com.yourcompany;

import java.util.List;
import java.util.ListIterator;

public class JumpSearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int n = list.size();
        if (n == 0) {
            long endTime = System.nanoTime();
            return new SearchResult<>("Jump Search", null, -1, counter, endTime - startTime);
        }

        int step = (int) Math.floor(Math.sqrt(n));
        int prev = 0;
        int currentStep = step;


        while (currentStep < n && list.get(currentStep - 1).compareTo(key) < 0) {
            counter++;
            prev = currentStep;
            currentStep += step;
        }


        ListIterator<T> iterator = list.listIterator(prev);
        for (int i = prev; i < Math.min(currentStep, n); i++) {
            counter++;
            T currentElement = iterator.next();
            if (currentElement.compareTo(key) == 0) {
                long endTime = System.nanoTime();
                return new SearchResult<>("Jump Search", currentElement, i, counter, endTime - startTime);
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult<>("Jump Search", null, -1, counter, endTime - startTime);
    }
}
