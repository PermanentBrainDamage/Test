package com.yourcompany;

import java.util.List;

public interface SearchAlgorithm<T extends Comparable<? super T>> {
    SearchResult<T> search(List<T> list, T key);
}