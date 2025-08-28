package com.yourcompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class Main {


    public static List<SearchResult<Article>> runEmpiricalTestsOnList(List<Article> list, int runs) {
        final SearchAlgorithm<Article> linear = new LinearSearch<>();
        final SearchAlgorithm<Article> binary = new BinarySearch<>();
        final SearchAlgorithm<Article> jump = new JumpSearch<>();
        final SearchAlgorithm<Article> interpolation = new InterpolationSearch<>();
        final Random random = new Random();

        List<SearchResult<Article>> allResults = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            Article target;
            if (random.nextDouble() < 0.2) {
                target = new Article(1_000_000 + i, "Non-existent", "Non-existent", 0, 0, 0, 0, 0);
            } else {
                target = list.get(random.nextInt(list.size()));
            }
            allResults.add(linear.search(list, target));
            allResults.add(binary.search(list, target));
            allResults.add(jump.search(list, target));
            allResults.add(interpolation.search(list, target));
        }

        String listType = list instanceof ArrayList ? "ArrayList" : "LinkedList";
        printStatistics(filterResults(allResults, "Linear Search"), "Linear Search", listType);
        printStatistics(filterResults(allResults, "Binary Search"), "Binary Search", listType);
        printStatistics(filterResults(allResults, "Jump Search"), "Jump Search", listType);
        printStatistics(filterResults(allResults, "InterpolationSearch"), "InterpolationSearch", listType);

        return allResults;
    }


    public static JFreeChart runRace(List<Article> list, String listType) {
        final SearchAlgorithm<Article> linear = new LinearSearch<>();
        final SearchAlgorithm<Article> binary = new BinarySearch<>();
        final SearchAlgorithm<Article> jump = new JumpSearch<>();
        final SearchAlgorithm<Article> interpolation = new InterpolationSearch<>();

        final Random random = new Random();
        final Article target = list.get(random.nextInt(list.size()));

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<SearchResult<Article>>> futures = new ArrayList<>();
        futures.add(executor.submit(() -> linear.search(list, target)));
        futures.add(executor.submit(() -> binary.search(list, target)));
        futures.add(executor.submit(() -> jump.search(list, target)));
        futures.add(executor.submit(() -> interpolation.search(list, target)));
        executor.shutdown();

        try {
            System.out.println("\n--- Race for target ID " + target.getId() + " | " + listType + " ---");

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//            boolean useLogAxis = listType.equals("LinkedList");
            double maxTime = 0;


            for (Future<SearchResult<Article>> f : futures) {
                SearchResult<Article> res = f.get();
                long timeNanos = res.getTimeNanos();


                double safeTime = Math.max((double)timeNanos, 1.0);

                if (safeTime > maxTime) {
                    maxTime = safeTime;
                }

                System.out.printf("Algorithm: %-20s | Time (ns): %d | Safe value for chart: %.1f%n", res.getAlgorithmName(), timeNanos, safeTime);
                dataset.addValue(safeTime, res.getAlgorithmName(), "Time");
            }


            JFreeChart chart = ChartFactory.createBarChart(
                    listType + " Race", "Algorithm", "Time (ns)",
                    dataset, PlotOrientation.VERTICAL, true, true, false
            );

//            if (useLogAxis) {
//                CategoryPlot plot = chart.getCategoryPlot();
//                LogarithmicAxis logAxis = new LogarithmicAxis("Time (ns) - Log Scale");
//                logAxis.setAllowNegativesFlag(false);
//
//
//                logAxis.setRange(0.9, maxTime * 1.1); // 從 0.9 開始，給最大值留出一些空間
//
//                plot.setRangeAxis(logAxis);
//            }

            return chart;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void printStatistics(List<SearchResult<Article>> results, String algorithm, String listType) {
        List<Integer> ops = results.stream().map(SearchResult::getOperations).collect(Collectors.toList());
        List<Long> times = results.stream().map(SearchResult::getTimeNanos).collect(Collectors.toList());

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

    private static List<SearchResult<Article>> filterResults(List<SearchResult<Article>> allResults, String algorithmName) {
        return allResults.stream()
                .filter(r -> r.getAlgorithmName().equals(algorithmName))
                .collect(Collectors.toList());
    }
}