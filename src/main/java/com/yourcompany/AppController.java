package com.yourcompany;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AppController {

    private final CardLayout cardLayout;
    private final JPanel chartPanelContainer;
    private final ChartPanel empiricalChartPanel;
    private final ChartPanel raceChartPanel;

    private final ArrayList<Article> arrayList = new ArrayList<>();
    private final LinkedList<Article> linkedList = new LinkedList<>();

    public AppController(CardLayout cardLayout, JPanel container, ChartPanel empiricalPanel, ChartPanel racePanel) {
        this.cardLayout = cardLayout;
        this.chartPanelContainer = container;
        this.empiricalChartPanel = empiricalPanel;
        this.raceChartPanel = racePanel;
        loadData();
    }

    private void loadData() {
        String filePath = "src/main/resources/Article.csv";
        DataInput.readArticleFile(arrayList, filePath);
        DataInput.readArticleFile(linkedList, filePath);
        Collections.sort(arrayList);
        Collections.sort(linkedList);
    }

    public void runSearchTask(String testType) {
        cardLayout.show(chartPanelContainer, "LOADING");

        List<Article> listToTest = "EMPIRICAL".equals(testType) ? arrayList : linkedList;
        SearchTask task = new SearchTask(listToTest, testType);

        task.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                try {
                    JFreeChart chart = task.get();
                    if (chart == null) return;

                    if ("EMPIRICAL".equals(testType)) {
                        empiricalChartPanel.setChart(chart);
                    } else {
                        raceChartPanel.setChart(chart);
                    }
                    cardLayout.show(chartPanelContainer, testType);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        task.execute();
    }

    public void showWorstCaseChart() {
        cardLayout.show(chartPanelContainer, "WORST_CASE");
    }
}