package com.yourcompany;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/Article.csv";
        ArrayList<Article> articleArrayList = new ArrayList<>();
        LinkedList<Article> articleLinkedList = new LinkedList<>();

        System.out.println("Reading data from " + filePath + "...");
        DataInput.readArticleFile(articleArrayList, filePath);
        DataInput.readArticleFile(articleLinkedList, filePath);

        if (articleArrayList.isEmpty()) {
            System.out.println("No articles found. Please check the file path and content.");
            return;
        }

        System.out.println("Successfully read " + articleArrayList.size() + " articles.");

        // Sort the lists after reading so that BinarySearch and others can work
        Collections.sort(articleArrayList);
        Collections.sort(articleLinkedList);

        System.out.println("\n--- Starting Empirical Testing ---");
        int numberOfRuns = 30;

        runTests(numberOfRuns, articleArrayList, "ArrayList");
        System.out.println("-----------------------------------");
        runTests(numberOfRuns, articleLinkedList, "LinkedList");

        System.out.println("\n--- Empirical Testing Complete ---");
    }

    private static <T extends Comparable<? super T>> void runTests(int numberOfRuns, List<T> list, String listType) {
        final SearchAlgorithm<T> linearSearch = new LinearSearch<>();
        final SearchAlgorithm<T> binarySearch = new BinarySearch<>();
        final SearchAlgorithm<T> jumpSearch = new JumpSearch<>();
        final SearchAlgorithm<T> interpolationSearch = new InterpolationSearch<>();

        final ExecutorService executor = Executors.newFixedThreadPool(4); // Use a thread pool

        List<Future<SearchResult<T>>> linearFutures = new ArrayList<>();
        List<Future<SearchResult<T>>> binaryFutures = new ArrayList<>();
        List<Future<SearchResult<T>>> jumpFutures = new ArrayList<>();
        List<Future<SearchResult<T>>> interpolationFutures = new ArrayList<>();

        final Random random = new Random();
        final int size = list.size();

        for (int i = 0; i < numberOfRuns; i++) {
            final T target;
            if (random.nextDouble() < 0.2) { // 20% chance of a non-existent key
                target = (T) new Article(random.nextInt(1000000) + size, "Non-existent", "Non-existent", 0, 0, 0, 0, 0);
            } else {
                target = list.get(random.nextInt(size));
            }

            linearFutures.add(executor.submit(() -> linearSearch.search(list, target)));
            binaryFutures.add(executor.submit(() -> binarySearch.search(list, target)));
            jumpFutures.add(executor.submit(() -> jumpSearch.search(list, target)));
            interpolationFutures.add(executor.submit(() -> interpolationSearch.search(list, target)));
        }

        executor.shutdown();

        try {
            printStatistics(getResults(linearFutures), "Linear Search", listType);
            printStatistics(getResults(binaryFutures), "Binary Search", listType);
            printStatistics(getResults(jumpFutures), "Jump Search", listType);
            printStatistics(getResults(interpolationFutures), "Interpolation Search", listType);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static <T extends Comparable<? super T>> List<SearchResult<T>> getResults(List<Future<SearchResult<T>>> futures) throws InterruptedException, ExecutionException {
        List<SearchResult<T>> results = new ArrayList<>();
        for (Future<SearchResult<T>> future : futures) {
            SearchResult<T> result = future.get();
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }

    private static <T extends Comparable<? super T>> void printStatistics(List<SearchResult<T>> results, String algorithmName, String listType) {
        System.out.println("\nAlgorithm: " + algorithmName);
        System.out.println("Data Structure: " + listType);
        System.out.println("-----------------------------------");

        List<Integer> operationsList = results.stream()
                .filter(r -> r.getOperations() > 0)
                .map(SearchResult::getOperations)
                .collect(Collectors.toList());

        List<Long> timeList = results.stream()
                .filter(r -> r.getTimeNanos() > 0)
                .map(SearchResult::getTimeNanos)
                .collect(Collectors.toList());

        if (operationsList.isEmpty() || timeList.isEmpty()) {
            System.out.println("No valid results to display.");
            return;
        }

        int bestOperations = operationsList.stream().min(Integer::compareTo).orElse(0);
        double meanOperations = operationsList.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        int worstOperations = operationsList.stream().max(Integer::compareTo).orElse(0);

        long bestTime = timeList.stream().min(Long::compareTo).orElse(0L);
        double meanTime = timeList.stream().mapToLong(Long::longValue).average().orElse(0.0);
        long worstTime = timeList.stream().max(Long::compareTo).orElse(0L);

        System.out.println("Best Case Operations: " + bestOperations);
        System.out.println("Mean Operations: " + String.format("%.2f", meanOperations));
        System.out.println("Worst Case Operations: " + worstOperations);

        System.out.println("Best Case Time (ns): " + bestTime);
        System.out.println("Mean Time (ns): " + String.format("%.2f", meanTime));
        System.out.println("Worst Case Time (ns): " + worstTime);
    }
}