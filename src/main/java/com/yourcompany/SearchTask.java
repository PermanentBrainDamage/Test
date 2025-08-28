package com.yourcompany;

import javax.swing.SwingWorker;
import org.jfree.chart.JFreeChart;
import java.util.List;

public class SearchTask extends SwingWorker<JFreeChart, Void> {

    private final List<Article> list;
    private final String testType;

    public SearchTask(List<Article> list, String testType) {
        this.list = list;
        this.testType = testType;
    }

    @Override
    protected JFreeChart doInBackground() throws Exception {
        if ("EMPIRICAL".equals(testType)) {
            // Run empirical tests and get the data
            List<SearchResult<Article>> results = Main.runEmpiricalTestsOnList(list, 30);
            String listType = (list instanceof java.util.ArrayList) ? "ArrayList" : "LinkedList";
            return ChartHelper.createEmpiricalChart(results, "Empirical Test: Mean Time on " + listType);
        } else if ("RACE".equals(testType)) {
            // Run race test
            String listType = (list instanceof java.util.ArrayList) ? "ArrayList" : "LinkedList";
            return Main.runRace(list, listType);
        }
        return null;
    }
}