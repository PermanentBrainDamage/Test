
package com.yourcompany;

import java.util.List;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class ChartHelper {


    public static JFreeChart createEmpiricalChart(List<SearchResult<Article>> results, String chartTitle) {
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


        return chart;
    }


    public static JFreeChart createWorstCaseLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int[] nValues = {100, 500, 1000, 2000, 4000, 8000, 16000};

        for (int n : nValues) {

            dataset.addValue((double) n, "O(n) - Linear", String.valueOf(n));
            dataset.addValue(Math.log(n) / Math.log(2), "O(log n) - Binary", String.valueOf(n));
            dataset.addValue(Math.sqrt(n), "O(sqrt(n)) - Jump", String.valueOf(n));
            dataset.addValue(Math.log(Math.log(n)) / Math.log(2), "O(log log n) - Interpolation (Avg)", String.valueOf(n));
        }


        JFreeChart chart = ChartFactory.createLineChart(
                "Theoretical Worst-Case Complexity (Log Scale)",
                "Problem Size (n)",
                "Number of Operations",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );


        CategoryPlot plot = chart.getCategoryPlot();
        LogarithmicAxis logAxis = new LogarithmicAxis("Operations (log scale)");
        logAxis.setAllowNegativesFlag(false);
        plot.setRangeAxis(logAxis);


        return chart;
    }
}