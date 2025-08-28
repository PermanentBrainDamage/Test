package com.yourcompany;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.CategoryPlot;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils; // <-- correct for 1.5.4
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartHelper {

    /** Create a race chart for a single run of each algorithm */
    public static void createRaceChart(List<SearchResult<Article>> results, String chartTitle, String fileName) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<String> algorithms = results.stream()
                .map(SearchResult::getAlgorithmName)
                .distinct()
                .collect(Collectors.toList());

        for (String algo : algorithms) {
            long time = results.stream()
                    .filter(r -> r.getAlgorithmName().equals(algo))
                    .mapToLong(SearchResult::getTimeNanos)
                    .findFirst()
                    .orElse(0L);

            dataset.addValue(time, algo, "Race Time (ns)");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                chartTitle,
                "Algorithm",
                "Time (ns)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        try {
            File chartFile = new File(fileName);
            ChartUtils.saveChartAsPNG(chartFile, chart, 800, 600); // <-- ChartUtils
            System.out.println("Race chart created at: " + chartFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error creating race chart: " + e.getMessage());
        }
    }

    /** Create an empirical chart showing mean time for multiple runs */
    public static void createEmpiricalChart(List<SearchResult<Article>> results, String chartTitle, String fileName) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<String> algorithms = results.stream()
                .map(SearchResult::getAlgorithmName)
                .distinct()
                .collect(Collectors.toList());

        for (String algo : algorithms) {
            double meanTime = results.stream()
                    .filter(r -> r.getAlgorithmName().equals(algo))
                    .mapToLong(SearchResult::getTimeNanos)
                    .average()
                    .orElse(0.0);

            dataset.addValue(meanTime, algo, "Mean Time (ns)");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                chartTitle,
                "Algorithm",
                "Mean Time (ns)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        try {
            File chartFile = new File(fileName);
            ChartUtils.saveChartAsPNG(chartFile, chart, 800, 600); // <-- ChartUtils
            System.out.println("Empirical chart created at: " + chartFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error creating empirical chart: " + e.getMessage());
        }
    }

    /** Optional: create worst-case time complexity chart */

    public static void createWorstCaseLineChartLog(String fileName) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int[] nValues = {100, 200, 400, 800, 1600, 3200, 6400};

        for (int n : nValues) {
            // ArrayList
            dataset.addValue(n, "Linear Search - ArrayList", String.valueOf(n));
            dataset.addValue(Math.log(n)/Math.log(2), "Binary Search - ArrayList", String.valueOf(n));
            dataset.addValue(Math.sqrt(n), "Jump Search - ArrayList", String.valueOf(n));
            dataset.addValue(n, "Interpolation Search - ArrayList", String.valueOf(n));

            // LinkedList
            dataset.addValue(n, "Linear Search - LinkedList", String.valueOf(n));
            dataset.addValue(n * (Math.log(n)/Math.log(2)), "Binary Search - LinkedList", String.valueOf(n));
            dataset.addValue(n * Math.sqrt(n), "Jump Search - LinkedList", String.valueOf(n));
            dataset.addValue((long)n * n, "Interpolation Search - LinkedList", String.valueOf(n));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Worst-Case Complexity vs Problem Size (Log Scale)",
                "Problem Size (n)",
                "Number of Operations",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Set logarithmic Y-axis
        CategoryPlot plot = chart.getCategoryPlot();
        LogarithmicAxis logAxis = new LogarithmicAxis("Number of Operations (log scale)");
        plot.setRangeAxis(logAxis);

        try {
            File chartFile = new File(fileName);
            ChartUtils.saveChartAsPNG(chartFile, chart, 1200, 700);
            System.out.println("Worst-case line chart (log scale) created at: " + chartFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error creating worst-case line chart: " + e.getMessage());
        }
    }


}
