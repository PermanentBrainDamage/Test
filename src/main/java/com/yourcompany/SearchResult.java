package com.yourcompany;

public class SearchResult<T> {
    private final String algorithmName;
    private final T foundItem;
    private final int index;
    private final int operations;
    private final long timeNanos;

    // A class to store the results of a search operation
    public SearchResult(String algorithmName, T foundItem, int index, int operations, long timeNanos) {
        this.algorithmName = algorithmName;
        this.foundItem = foundItem;
        this.index = index;
        this.operations = operations;
        this.timeNanos = timeNanos;
    }

    // Returns the name of the algorithm used
    public String getAlgorithmName() {
        return algorithmName;
    }

    // Returns the found item or null
    public T getFoundItem() {
        return foundItem;
    }

    // Returns the index of the item
    public int getIndex() {
        return index;
    }

    // Returns the number of operations
    public int getOperations() {
        return operations;
    }

    // Returns the time taken in nanoseconds
    public long getTimeNanos() {
        return timeNanos;
    }
}