package com.yourcompany;

import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public class JumpSearch<T extends Comparable<? super T>> implements SearchAlgorithm<T> {

    @Override
    public SearchResult<T> search(List<T> list, T key) {
        int n = list.size();
        if (n == 0) {
            return new SearchResult<>("Jump Search", null, -1, 0, 0L);
        }


        if (list instanceof RandomAccess) {
            return searchForRandomAccess(list, key);
        } else {
            return searchForSequentialAccess(list, key);
        }
    }

    private SearchResult<T> searchForRandomAccess(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int n = list.size();
        int step = (int) Math.floor(Math.sqrt(n));
        int prev = 0;


        while (prev < n && list.get(Math.min(step, n) - 1).compareTo(key) < 0) {
            counter++;
            prev = step;
            step += (int) Math.floor(Math.sqrt(n));
            if (prev >= n) {
                long endTime = System.nanoTime();
                return new SearchResult<>("Jump Search", null, -1, counter, endTime - startTime);
            }
        }

        // 在块内进行线性搜索
        for (int i = prev; i < Math.min(step, n); i++) {
            counter++;
            if (list.get(i).compareTo(key) == 0) {
                long endTime = System.nanoTime();
                return new SearchResult<>("Jump Search", list.get(i), i, counter, endTime - startTime);
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult<>("Jump Search", null, -1, counter, endTime - startTime);
    }


    private SearchResult<T> searchForSequentialAccess(List<T> list, T key) {
        long startTime = System.nanoTime();
        int counter = 0;
        int n = list.size();
        int stepSize = (int) Math.floor(Math.sqrt(n));
        int prevIndex = 0;

        ListIterator<T> iterator = list.listIterator();
        T currentElement = iterator.next();


        while (true) {
            counter++;
            int currentIndex = iterator.previousIndex();
            int nextJumpIndex = Math.min(currentIndex + stepSize, n - 1);


            if (currentElement.compareTo(key) >= 0) {
                iterator.previous();
                break;
            }


            if (!iterator.hasNext() || currentIndex >= n - 1) {
                break;
            }


            int jumps = nextJumpIndex - currentIndex;
            for (int i = 0; i < jumps && iterator.hasNext(); i++) {
                currentElement = iterator.next();
            }


            if (currentElement.compareTo(key) < 0) {
                prevIndex = iterator.previousIndex();
            } else {
                iterator.previous();
                break;
            }
        }



        iterator = list.listIterator(prevIndex);
        for (int i = prevIndex; i < Math.min(prevIndex + stepSize + 1, n) && iterator.hasNext(); i++) {
            currentElement = iterator.next();
            counter++;
            if (currentElement.compareTo(key) == 0) {
                long endTime = System.nanoTime();
                return new SearchResult<>("Jump Search", currentElement, i, counter, endTime - startTime);
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult<>("Jump Search", null, -1, counter, endTime - startTime);
    }
}
