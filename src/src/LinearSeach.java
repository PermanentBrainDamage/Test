package src;

import java.util.List;

class LinearSearch<T> implements SearchAlgorithm<T> {
    @Override
    public SearchResult search(List<T> list, T target) {
        long startTime = System.nanoTime();
        int comparisons = 0;

        for (int i = 0; i < list.size(); i++) {
            comparisons++;
            if (list.get(i).equals(target)) {
                long endTime = System.nanoTime();
                return new SearchResult(i, comparisons, endTime - startTime);
            }
        }
        long endTime = System.nanoTime();
        return new SearchResult(-1, comparisons, endTime - startTime);
    }
}