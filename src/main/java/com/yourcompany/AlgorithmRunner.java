package com.yourcompany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class AlgorithmRunner {

    // Runs empirical tests on a list with different search algorithms
    public static JFreeChart runEmpiricalTestsOnList(List<Article> list, int runs) {
        List<Article> sortedList;
        if (list instanceof java.util.RandomAccess) {
            sortedList = new ArrayList<>(list);
            Collections.sort(sortedList);
        } else {
            sortedList = new ArrayList<>(list);
            Collections.sort(sortedList);
        }

        final SearchAlgorithm<Article> linear = new LinearSearch<>();
        final SearchAlgorithm<Article> binary = new BinarySearch<>();
        final SearchAlgorithm<Article> jump = new JumpSearch<>();
        final SearchAlgorithm<Article> interpolation = new InterpolationSearch<>();
        final Random random = new Random();

        List<SearchResult<Article>> allResults = new ArrayList<>();

        // Executes search runs with a mix of existing and non-existent targets
        for (int i = 0; i < runs; i++) {
            Article target;
            if (random.nextDouble() < 0.2) {
                target = new Article(1_000_000 + i, "Non-existent", "Non-existent", 0, 0, 0, 0, 0);
            } else {
                target = sortedList.get(random.nextInt(sortedList.size()));
            }

            allResults.add(linear.search(list, target));
            allResults.add(binary.search(sortedList, target));
            allResults.add(jump.search(sortedList, target));
            allResults.add(interpolation.search(sortedList, target));
        }

        String listType = list instanceof ArrayList ? "ArrayList" : "LinkedList";
        String chartTitle = String.format("30 Runs Empirical Test on %s (with ~20%% non-existent keys)", listType);

        return ChartHelper.createEmpiricalChart(allResults, chartTitle);
    }

    // Runs a single race test for a random target
    public static JFreeChart runRace(List<Article> list, String listType) {
        List<Article> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);

        final SearchAlgorithm<Article> linear = new LinearSearch<>();
        final SearchAlgorithm<Article> binary = new BinarySearch<>();
        final SearchAlgorithm<Article> jump = new JumpSearch<>();
        final SearchAlgorithm<Article> interpolation = new InterpolationSearch<>();

        final Random random = new Random();
        final Article target = sortedList.get(random.nextInt(sortedList.size()));

        // Uses a thread pool to run algorithms concurrently
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<SearchResult<Article>>> futures = new ArrayList<>();

        futures.add(executor.submit(() -> linear.search(list, target)));
        futures.add(executor.submit(() -> binary.search(sortedList, target)));
        futures.add(executor.submit(() -> jump.search(sortedList, target)));
        futures.add(executor.submit(() -> interpolation.search(sortedList, target)));
        executor.shutdown();

        try {
            System.out.println("\n--- Race for target ID " + target.getId() + " | " + listType + " ---");

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            boolean useLogAxis = listType.equals("LinkedList");
            double maxTime = 0;

            // Gathers results and prints statistics
            for (Future<SearchResult<Article>> f : futures) {
                SearchResult<Article> res = f.get();
                long timeNanos = res.getTimeNanos();

                double safeTime = Math.max((double)timeNanos, 1.0);
                if (safeTime > maxTime) {
                    maxTime = safeTime;
                }

                System.out.printf("Algorithm: %-20s | Time (ns): %d | Operations: %d%n",
                        res.getAlgorithmName(), timeNanos, res.getOperations());
                dataset.addValue(safeTime, res.getAlgorithmName(), "Time");
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    listType + " Race", "Algorithm", "Time (ns)",
                    dataset, PlotOrientation.VERTICAL, true, true, false
            );

            if (useLogAxis) {
                CategoryPlot plot = chart.getCategoryPlot();
                LogarithmicAxis logAxis = new LogarithmicAxis("Time (ns) - Log Scale");
                logAxis.setAllowNegativesFlag(false);
                logAxis.setRange(0.9, maxTime * 1.1);
                plot.setRangeAxis(logAxis);
            }

            return chart;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to print detailed statistics
    private static void printStatistics(List<SearchResult<Article>> results, String algorithm, String listType) {
        List<Integer> ops = results.stream().map(SearchResult::getOperations).toList();
        List<Long> times = results.stream().map(SearchResult::getTimeNanos).toList();

        int bestOps = ops.stream().min(Integer::compareTo).orElse(0);
        int worstOps = ops.stream().max(Integer::compareTo).orElse(0);
        double meanOps = ops.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        long bestTime = times.stream().min(Long::compareTo).orElse(0L);
        long worstTime = times.stream().max(Long::compareTo).orElse(0L);
        double meanTime = times.stream().mapToLong(Long::longValue).average().orElse(0.0);

        System.out.printf("\n--- Stats for %s on %s ---%n", algorithm, listType);
        System.out.printf("Ops -> Best: %-10d | Mean: %-15.2f | Worst: %d%n", bestOps, meanOps, worstOps);
        System.out.printf("Time (ns) -> Best: %-10d | Mean: %-15.2f | Worst: %d%n", bestTime, meanTime, worstTime);
    }

    // Method to filter results by algorithm name
    private static List<SearchResult<Article>> filterResults(List<SearchResult<Article>> allResults, String algorithmName) {
        return allResults.stream()
                .filter(r -> r.getAlgorithmName().equals(algorithmName))
                .collect(Collectors.toList());
    }
}