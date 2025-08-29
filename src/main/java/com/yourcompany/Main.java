package com.yourcompany;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class Main extends JFrame {

    public Main() {
        setTitle("Search Algorithm Analyzer");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        JPanel chartPanelContainer = new JPanel(cardLayout);
        ChartPanel empiricalChartPanel = new ChartPanel(null);
        ChartPanel raceChartPanel = new ChartPanel(null);

        AppController controller = new AppController(cardLayout, chartPanelContainer, empiricalChartPanel, raceChartPanel);

        JPanel buttonPanel = UIBuilder.createButtonPanel(
                e -> controller.runSearchTask("EMPIRICAL"),
                e -> controller.runSearchTask("RACE"),
                e -> controller.showWorstCaseChart()
        );
        JPanel loadingPanel = UIBuilder.createLoadingPanel();

        JFreeChart worstCaseChart = ChartHelper.createWorstCaseLineChart();
        ChartPanel worstCaseChartPanel = new ChartPanel(worstCaseChart);

        chartPanelContainer.add(new JPanel(), "EMPTY");
        chartPanelContainer.add(empiricalChartPanel, "EMPIRICAL");
        chartPanelContainer.add(raceChartPanel, "RACE");
        chartPanelContainer.add(worstCaseChartPanel, "WORST_CASE");
        chartPanelContainer.add(loadingPanel, "LOADING");

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(chartPanelContainer, BorderLayout.CENTER);

        cardLayout.show(chartPanelContainer, "EMPTY");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main gui = new Main();
            gui.setVisible(true);
        });
    }
}