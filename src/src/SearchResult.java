package src;

class SearchResult {
    private final int index;
    private final int comparisons;
    private final long timeNanos;

    public SearchResult(int index, int comparisons, long timeNanos) {
        this.index = index;
        this.comparisons = comparisons;
        this.timeNanos = timeNanos;
    }

    public int getIndex() { return index; }
    public int getComparisons() { return comparisons; }
    public long getTimeNanos() { return timeNanos; }
}