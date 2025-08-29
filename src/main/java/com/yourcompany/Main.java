package com.yourcompany;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class Main extends JFrame {

    // Main constructor to set up the GUI
    public Main() {
        setTitle("Search Algorithm Analyzer");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        JPanel chartPanelContainer = new JPanel(cardLayout);
        ChartPanel empiricalChartPanel = new ChartPanel(null);
        ChartPanel raceChartPanel = new ChartPanel(null);

        // Create dropdown
        String[] listTypes = {"ArrayList", "LinkedList"};
        JComboBox<String> listTypeSelector = new JComboBox<>(listTypes);

        AppController controller = new AppController(cardLayout, chartPanelContainer, empiricalChartPanel, raceChartPanel, listTypeSelector);

        JPanel buttonPanel = UIBuilder.createButtonPanel(
                e -> controller.runSearchTask("EMPIRICAL"),
                e -> controller.runSearchTask("RACE"),
                e -> controller.showWorstCaseChart(),
                listTypeSelector
        );

        // Create loading panel
        JPanel loadingPanel = UIBuilder.createLoadingPanel();

        // Create worst-case chart
        JFreeChart worstCaseChart = ChartHelper.createWorstCaseLineChart();
        ChartPanel worstCaseChartPanel = new ChartPanel(worstCaseChart);

        chartPanelContainer.add(new JPanel(), "EMPTY");
        chartPanelContainer.add(empiricalChartPanel, "EMPIRICAL");
        chartPanelContainer.add(raceChartPanel, "RACE");
        chartPanelContainer.add(worstCaseChartPanel, "WORST_CASE");
        chartPanelContainer.add(loadingPanel, "LOADING");

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(chartPanelContainer, BorderLayout.CENTER);

        // Sets the initial view to be empty
        cardLayout.show(chartPanelContainer, "EMPTY");
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main gui = new Main();
            gui.setVisible(true);
        });
    }
}