package com.yourcompany;

import java.util.List;


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
                .toList();

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
        int[] ns = {1, 10, 50, 100, 1000, 5000, 10000, 20000};

        for (int n : ns) {

            dataset.addValue(Math.max(1.0, n), "O(n) Linear/Interpolation(Worst) - ArrayList", String.valueOf(n));
            dataset.addValue(Math.max(1.0, Math.log(n)/Math.log(2)), "O(log n) Binary Search - ArrayList", String.valueOf(n));
            dataset.addValue(Math.max(1.0, Math.sqrt(n)), "O(sqrt n) Jump Search - ArrayList", String.valueOf(n));


            dataset.addValue(Math.max(1.0, n), "O(n) Linear/Jump - LinkedList", String.valueOf(n));


            dataset.addValue(Math.max(1.0, n * (Math.log(n)/Math.log(2))), "O(n log n) Binary Search - LinkedList", String.valueOf(n));


            dataset.addValue(Math.max(1.0, n * Math.sqrt(n)), "O(n sqrt n) Interpolation(Worst) - LinkedList", String.valueOf(n));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Worst-Case Complexity (Theoretical)",
                "n (Input Size)", "Operations (log scale)",
                dataset, PlotOrientation.VERTICAL, true, true, false
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeAxis(new LogarithmicAxis("Operations (log scale)"));
        return chart;
    }


}