package com.yourcompany;

import java.util.List;

public class SearchTask<T extends Comparable<? super T>> implements Runnable {
    private final SearchAlgorithm<T> algorithm;
    private final List<T> list;
    private final T target;
    private SearchResult<T> result;

    public SearchTask(SearchAlgorithm<T> algorithm, List<T> list, T target) {
        this.algorithm = algorithm;
        this.list = list;
        this.target = target;
    }

    @Override
    public void run() {
        this.result = algorithm.search(list, target);
    }

    public SearchResult<T> getResult() {
        return result;
    }
}