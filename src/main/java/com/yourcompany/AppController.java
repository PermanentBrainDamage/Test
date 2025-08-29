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
    private final JComboBox<String> listTypeSelector;

    private final ArrayList<Article> arrayList = new ArrayList<>();
    private final LinkedList<Article> linkedList = new LinkedList<>();

    // Constructor to set up UI components
    public AppController(CardLayout cardLayout, JPanel container, ChartPanel empiricalPanel, ChartPanel racePanel, JComboBox<String> selector) {
        this.cardLayout = cardLayout;
        this.chartPanelContainer = container;
        this.empiricalChartPanel = empiricalPanel;
        this.raceChartPanel = racePanel;
        this.listTypeSelector = selector;
        loadData();
    }

    // Loads data into both ArrayList and LinkedList
    private void loadData() {
        String filePath = "src/main/resources/Article.csv";
        DataInput.readArticleFile(arrayList, filePath);
        DataInput.readArticleFile(linkedList, filePath);
    }

    // Runs a search task and updates the chart
    public void runSearchTask(String testType) {
        cardLayout.show(chartPanelContainer, "LOADING");

        String selectedListType = (String) listTypeSelector.getSelectedItem();
        List<Article> listToTest;

        // Chooses the list to test
        if ("LinkedList".equals(selectedListType)) {
            listToTest = linkedList;
        } else {
            listToTest = arrayList;
        }

        SearchTask task = new SearchTask(listToTest, testType);

        // Updates the chart panel upon task completion
        task.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                try {
                    JFreeChart chart = task.get();
                    if (chart == null) return;

                    ChartPanel panelToUpdate = "EMPIRICAL".equals(testType) ? empiricalChartPanel : raceChartPanel;
                    panelToUpdate.setChart(chart);

                    cardLayout.show(chartPanelContainer, testType);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        task.execute();
    }

    // Displays the worst-case chart
    public void showWorstCaseChart() {
        cardLayout.show(chartPanelContainer, "WORST_CASE");
    }
}