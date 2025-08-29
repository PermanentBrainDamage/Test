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

            return AlgorithmRunner.runEmpiricalTestsOnList(list, 30);
        } else if ("RACE".equals(testType)) {

            String listType = (list instanceof java.util.ArrayList) ? "ArrayList" : "LinkedList";
            return AlgorithmRunner.runRace(list, listType);
        }
        return null;
    }
}