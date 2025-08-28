package com.yourcompany;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        ArrayList<Article> arrayList = new ArrayList<>();
        LinkedList<Article> linkedList = new LinkedList<>();
        String filePath = "src/main/resources/Article.csv";

        DataInput.readArticleFile(arrayList, filePath);
        DataInput.readArticleFile(linkedList, filePath);

        if (arrayList.isEmpty()) {
            System.out.println("No articles found. Check the file path.");
            return;
        }

        Collections.sort(arrayList);
        Collections.sort(linkedList);

        while (choice != 4) {
            displayMenu();
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> runEmpiricalTests(arrayList, linkedList);
                    case 2 -> displayWorstCaseTable();
                    case 3 -> runRaceMenu(arrayList, linkedList, scanner);
                    case 4 -> System.out.println("Exiting program. Goodbye!");
                    default -> System.out.println("Invalid choice. Enter 1-4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next();
                choice = -1;
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("--- Search Algorithm Tester ---");
        System.out.println("1. Run Empirical Testing (30 runs)");
        System.out.println("2. Worst Case Time Complexity Table");
        System.out.println("3. Race Algorithms (Single run)");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    // ---------------- EMPIRICAL TESTING ----------------
    private static void runEmpiricalTests(List<Article> arrayList, List<Article> linkedList) {
        System.out.println("\n--- Empirical Testing (30 runs) ---");

        System.out.println("\n--- ArrayList ---");
        runTests(30, arrayList, "ArrayList");

        System.out.println("\n--- LinkedList ---");
        runTests(30, linkedList, "LinkedList");
    }

    private static void runTests(int runs, List<Article> list, String listType) {
        final SearchAlgorithm<Article> linear = new LinearSearch<>();
        final SearchAlgorithm<Article> binary = new BinarySearch<>();
        final SearchAlgorithm<Article> jump = new JumpSearch<>();
        final SearchAlgorithm<Article> interpolation = new InterpolationSearch<>();

        final Random random = new Random();

        List<SearchResult<Article>> linearResults = new ArrayList<>();
        List<SearchResult<Article>> binaryResults = new ArrayList<>();
        List<SearchResult<Article>> jumpResults = new ArrayList<>();
        List<SearchResult<Article>> interpolationResults = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            Article target;
            if (random.nextDouble() < 0.2) { // 20% non-existent
                target = new Article(1_000_000 + i, "Non-existent", "Non-existent",0,0,0,0,0);
            } else {
                target = list.get(random.nextInt(list.size()));
            }

            linearResults.add(linear.search(list, target));
            binaryResults.add(binary.search(list, target));
            jumpResults.add(jump.search(list, target));
            interpolationResults.add(interpolation.search(list, target));
        }

        printStatistics(linearResults, "Linear Search", listType);
        printStatistics(binaryResults, "Binary Search", listType);
        printStatistics(jumpResults, "Jump Search", listType);
        printStatistics(interpolationResults, "Interpolation Search", listType);
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

        System.out.printf("\n%s | %s%n", algorithm, listType);
        System.out.printf("Ops -> Best: %d | Mean: %.2f | Worst: %d%n", bestOps, meanOps, worstOps);
        System.out.printf("Time (ns) -> Best: %d | Mean: %.2f | Worst: %d%n", bestTime, meanTime, worstTime);
    }

    // ---------------- RACE TEST ----------------
    private static void runRaceMenu(List<Article> arrayList, List<Article> linkedList, Scanner scanner) {
        System.out.println("Select Data Structure:");
        System.out.println("1. ArrayList");
        System.out.println("2. LinkedList");
        int choice = scanner.nextInt();
        if (choice == 1) runRace(arrayList, "ArrayList");
        else if (choice == 2) runRace(linkedList, "LinkedList");
        else System.out.println("Invalid choice.");
    }

    private static void runRace(List<Article> list, String listType) {
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

            // Populate dataset and replace zero or negative times with 1
            for (Future<SearchResult<Article>> f : futures) {
                SearchResult<Article> res = f.get();
                long safeTime = Math.max(res.getTimeNanos(), 1);
                System.out.printf("Algorithm: %s | Time (ns): %d%n", res.getAlgorithmName(), res.getTimeNanos());
                dataset.addValue(safeTime, res.getAlgorithmName(), listType);
            }

            // Create chart
            JFreeChart chart = ChartFactory.createBarChart(
                    listType + " Race",
                    "Algorithm",
                    "Time (ns)",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false
            );

            // Apply logarithmic axis for LinkedList
            if (listType.equals("LinkedList")) {
                CategoryPlot plot = chart.getCategoryPlot();
                LogarithmicAxis logAxis = new LogarithmicAxis("Time (ns)");
                logAxis.setAllowNegativesFlag(false);
                logAxis.setStrictValuesFlag(false);
                logAxis.setRange(1, getMaxTime(dataset) * 1.1); // safe upper bound
                plot.setRangeAxis(logAxis);
            }

            ChartUtils.saveChartAsPNG(new File(listType + "_race_chart.png"), chart, 800, 600);
            System.out.println("Race chart saved as " + listType + "_race_chart.png");

        } catch (InterruptedException | ExecutionException | java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // Helper function to get maximum value in dataset
    private static double getMaxTime(DefaultCategoryDataset dataset) {
        double max = Double.MIN_VALUE;
        for (int row = 0; row < dataset.getRowCount(); row++) {
            for (int col = 0; col < dataset.getColumnCount(); col++) {
                Number value = dataset.getValue(row, col);
                if (value != null) {
                    max = Math.max(max, value.doubleValue());
                }
            }
        }
        return max;
    }



    // ---------------- WORST CASE ----------------
    private static void displayWorstCaseTable() {
        System.out.println("\n--- Worst Case / Best / Mean Operations ---");
        System.out.printf("%-20s | %-15s | %-15s | %-15s | %-15s%n",
                "Algorithm", "List Type", "Best Ops", "Mean Ops", "Worst Ops");
        System.out.println("--------------------|----------------|----------------|----------------|----------------");

        ArrayList<Article> arrayList = new ArrayList<>();
        LinkedList<Article> linkedList = new LinkedList<>();
        String filePath = "src/main/resources/Article.csv";

        DataInput.readArticleFile(arrayList, filePath);
        DataInput.readArticleFile(linkedList, filePath);

        Collections.sort(arrayList);
        Collections.sort(linkedList);

        List<SearchAlgorithm<Article>> algorithms = List.of(
                new LinearSearch<>(),
                new BinarySearch<>(),
                new JumpSearch<>(),
                new InterpolationSearch<>()
        );
        List<String> algorithmNames = List.of(
                "Linear Search",
                "Binary Search",
                "Jump Search",
                "Interpolation Search"
        );

        List<List<Article>> lists = List.of(arrayList, linkedList);
        List<String> listNames = List.of("ArrayList", "LinkedList");

        // Simulate worst-case: pick last element (or non-existent element)
        for (List<Article> list : lists) {
            String listType = list instanceof ArrayList ? "ArrayList" : "LinkedList";
            Article worstCaseTarget = new Article(1_000_000, "Non-existent", "Non-existent",0,0,0,0,0);

            for (int i = 0; i < algorithms.size(); i++) {
                SearchResult<Article> result = algorithms.get(i).search(list, worstCaseTarget);

                // In worst-case simulation, all ops count as worst
                int ops = result.getOperations();
                System.out.printf("%-20s | %-15s | %-15d | %-15.2f | %-15d%n",
                        algorithmNames.get(i),
                        listType,
                        ops,      // best ops = worst in this simplified simulation
                        (double) ops, // mean ops = same for single run simulation
                        ops
                );
            }
        }
    }

}
