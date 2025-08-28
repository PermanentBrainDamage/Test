package com.yourcompany;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class MainGUI extends JFrame {

    private final JPanel chartPanelContainer;
    private final CardLayout cardLayout;

    private final ArrayList<Article> arrayList = new ArrayList<>();
    private final LinkedList<Article> linkedList = new LinkedList<>();

    public MainGUI() {
        setTitle("Search Algorithm Analyzer");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load data
        String filePath = "src/main/resources/Article.csv";
        DataInput.readArticleFile(arrayList, filePath);
        DataInput.readArticleFile(linkedList, filePath);
        Collections.sort(arrayList);
        Collections.sort(linkedList);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton empiricalButton = new JButton("Run Empirical Test (ArrayList)");
        JButton raceButton = new JButton("Run Race Test (LinkedList)");
        JButton worstCaseButton = new JButton("Show Worst-Case Complexity");

        buttonPanel.add(empiricalButton);
        buttonPanel.add(raceButton);
        buttonPanel.add(worstCaseButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Chart display panel using CardLayout
        cardLayout = new CardLayout();
        chartPanelContainer = new JPanel(cardLayout);
        mainPanel.add(chartPanelContainer, BorderLayout.CENTER);

        // Add action listeners
        empiricalButton.addActionListener(e -> runSearchTask("EMPIRICAL"));
        raceButton.addActionListener(e -> runSearchTask("RACE"));
        worstCaseButton.addActionListener(e -> showWorstCaseChart());

        add(mainPanel);
    }

    private void runSearchTask(String testType) {
        // Use SearchTask to run tests in the background
        SearchTask task = new SearchTask(testType.equals("EMPIRICAL") ? arrayList : linkedList, testType);

        // A simple loading indicator
        JPanel loadingPanel = new JPanel();
        loadingPanel.add(new JLabel("Running tests, please wait..."));
        chartPanelContainer.add(loadingPanel, "loading");
        cardLayout.show(chartPanelContainer, "loading");

        task.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                try {
                    JFreeChart chart = task.get();
                    if (chart != null) {
                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanelContainer.add(chartPanel, testType);
                        cardLayout.show(chartPanelContainer, testType);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        task.execute();
    }

    private void showWorstCaseChart() {
        JFreeChart chart = ChartHelper.createWorstCaseLineChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanelContainer.add(chartPanel, "WORST_CASE");
        cardLayout.show(chartPanelContainer, "WORST_CASE");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}