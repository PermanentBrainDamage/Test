package src;

import java.util.List;

class JumpSearch<T extends Comparable<T>> implements SearchAlgorithm<T> {
    @Override
    public SearchResult search(List<T> list, T target) {
        long startTime = System.nanoTime();
        int comparisons = 0;
        int n = list.size();
        int step = (int) Math.floor(Math.sqrt(n));
        int prev = 0;

        while (prev < n && list.get(Math.min(step, n) - 1).compareTo(target) < 0) {
            comparisons++;
            prev = step;
            step += (int) Math.floor(Math.sqrt(n));
            if (prev >= n) {
                long endTime = System.nanoTime();
                return new SearchResult(-1, comparisons, endTime - startTime);
            }
        }

        while (prev < Math.min(step, n)) {
            comparisons++;
            if (list.get(prev).compareTo(target) == 0) {
                long endTime = System.nanoTime();
                return new SearchResult(prev, comparisons, endTime - startTime);
            }
            prev++;
        }

        long endTime = System.nanoTime();
        return new SearchResult(-1, comparisons, endTime - startTime);
    }
}