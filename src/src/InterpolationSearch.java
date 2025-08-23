package src;

import java.util.List;

class InterpolationSearch<T extends Comparable<T>> implements SearchAlgorithm<T> {
    @Override
    public SearchResult search(List<T> list, T target) {
        long startTime = System.nanoTime();
        int comparisons = 0;
        int low = 0;
        int high = list.size() - 1;

        while (low <= high && target.compareTo(list.get(low)) >= 0 && target.compareTo(list.get(high)) <= 0) {
            comparisons++;

            // Check for the division by zero condition
            if (list.get(high).compareTo(list.get(low)) == 0) {
                // If low and high elements are the same, they must also be equal to the target
                // for the target to be in the list. This handles cases with duplicate values.
                if (list.get(low).compareTo(target) == 0) {
                    long endTime = System.nanoTime();
                    return new SearchResult(low, comparisons, endTime - startTime);
                } else {
                    // Target is not in this range
                    break;
                }
            }
            
            // This is the core Interpolation Search formula.
            // It estimates the position based on the relative position of the target.
            long pos = low; 
            try {
                int idLow = ((Article) list.get(low)).getId();
                int idHigh = ((Article) list.get(high)).getId();
                int idTarget = ((Article) target).getId();
                
                pos = low + (long) (((double) (idTarget - idLow) / (idHigh - idLow)) * (high - low));
            } catch (ClassCastException | ArithmeticException e) {
                // Fallback for non-Article types or if the IDs are the same (already handled above)
                pos = low + (high - low) / 2;
            }

            // Ensure the calculated position is within bounds
            if (pos < low || pos > high) {
                 pos = low + (high - low) / 2; // Fall back to binary search if out of bounds
            }
            
            int comparison = list.get((int) pos).compareTo(target);
            if (comparison == 0) {
                long endTime = System.nanoTime();
                return new SearchResult((int) pos, comparisons, endTime - startTime);
            } else if (comparison < 0) {
                low = (int) pos + 1;
            } else {
                high = (int) pos - 1;
            }
        }
        
        long endTime = System.nanoTime();
        return new SearchResult(-1, comparisons, endTime - startTime);
    }
}