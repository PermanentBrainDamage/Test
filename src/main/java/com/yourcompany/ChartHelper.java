package com.yourcompany;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartHelper {

    // Creates a bar chart showing the mean time of different search algorithms.
    public static JFreeChart createEmpiricalChart(List<SearchResult<Article>> results, String chartTitle) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<String> algorithms = results.stream()
                .map(SearchResult::getAlgorithmName)
                .distinct()
                .toList();

        // Calculates the average time for each algorithm and adds it to the dataset.
        for (String algo : algorithms) {
            double meanTime = results.stream()
                    .filter(r -> r.getAlgorithmName().equals(algo))
                    .mapToLong(SearchResult::getTimeNanos)
                    .average()
                    .orElse(0.0);

            dataset.addValue(meanTime, algo, "Mean Time (ns)");
        }

        // Creates and returns the bar chart.
        JFreeChart chart = ChartFactory.createBarChart(
                chartTitle,
                "Algorithm",
                "Mean Time (ns)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        return chart;
    }

    // Creates a line chart to visualize the theoretical worst-case time complexity of algorithms.
    public static JFreeChart createWorstCaseLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Defines different input sizes (n) for the chart.
        int[] ns = {2, 10, 50, 100, 500, 1000, 5000, 10000, 20000};

        // Populates the dataset with theoretical complexity values for each algorithm.
        for (int n : ns) {
            // ArrayList complexities
            dataset.addValue(Math.max(1.0, (double)n), "Linear / Interpolation (Worst) [O(n)]", String.valueOf(n));
            dataset.addValue(Math.max(1.0, Math.sqrt(n)), "Jump Search [O(sqrt n)]", String.valueOf(n));
            dataset.addValue(Math.max(1.0, Math.log(n) / Math.log(2)), "Binary Search [O(log n)]", String.valueOf(n));

            // LinkedList complexities
            dataset.addValue(Math.max(1.0, (double)n), "Linear Search [O(n)] - LinkedList", String.valueOf(n));
            dataset.addValue(Math.max(1.0, (double)n * (Math.log(n) / Math.log(2))), "Binary Search [O(n log n)] - LinkedList", String.valueOf(n));
            dataset.addValue(Math.max(1.0, Math.pow(n, 2)), "Interpolation (Worst) [O(n^2)] - LinkedList", String.valueOf(n));
        }

        // Creates the line chart with a logarithmic y-axis for better visualization.
        JFreeChart chart = ChartFactory.createLineChart(
                "Worst-Case Time Complexity (Theoretical)",
                "n (Input Size)", "Operations (log scale)",
                dataset, PlotOrientation.VERTICAL, true, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        LogarithmicAxis logAxis = new LogarithmicAxis("Operations (log scale)");
        logAxis.setAllowNegativesFlag(false);
        plot.setRangeAxis(logAxis);
        return chart;
    }
}