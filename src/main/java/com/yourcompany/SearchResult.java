package com.yourcompany;

public class SearchResult<T> {
    private T foundObject;
    private int index;
    private int operations;
    private long timeNanos;

    public SearchResult(T foundObject, int index, int operations, long timeNanos) {
        this.foundObject = foundObject;
        this.index = index;
        this.operations = operations;
        this.timeNanos = timeNanos;
    }

    public T getFoundObject() {
        return foundObject;
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