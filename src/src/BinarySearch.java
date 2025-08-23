package src;

import java.util.List;

class BinarySearch<T extends Comparable<T>> implements SearchAlgorithm<T> {
    @Override
    public SearchResult search(List<T> list, T target) {
        long startTime = System.nanoTime();
        int comparisons = 0;
        int left = 0, right = list.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;

            int comparison = list.get(mid).compareTo(target);
            if (comparison == 0) {
                long endTime = System.nanoTime();
                return new SearchResult(mid, comparisons, endTime - startTime);
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        long endTime = System.nanoTime();
        return new SearchResult(-1, comparisons, endTime - startTime);
    }
}