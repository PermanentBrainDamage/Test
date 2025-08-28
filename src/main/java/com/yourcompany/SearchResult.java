package com.yourcompany;

public class SearchResult<T> {
    private final String algorithmName;
    private final T foundItem;
    private final int index;
    private final int operations;
    private final long timeNanos;

    public SearchResult(String algorithmName, T foundItem, int index, int operations, long timeNanos) {
        this.algorithmName = algorithmName;
        this.foundItem = foundItem;
        this.index = index;
        this.operations = operations;
        this.timeNanos = timeNanos;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public T getFoundItem() {
        return foundItem;
    }

    public int getIndex() {
        return index;
    }

    public int getOperations() {
        return operations;
    }

    public long getTimeNanos() {
        return timeNanos;
    }
}
