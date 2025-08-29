package com.yourcompany;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.BorderLayout;

public class UIBuilder {

    public static JPanel createButtonPanel(ActionListener empiricalListener, ActionListener raceListener, ActionListener worstCaseListener, JComboBox<String> selector) {
        JPanel panel = new JPanel();
        JButton empiricalButton = new JButton("Run Empirical Test");
        empiricalButton.addActionListener(empiricalListener);

        JButton raceButton = new JButton("Run Race Test");
        raceButton.addActionListener(raceListener);

        JButton worstCaseButton = new JButton("Show Worst-Case Chart");
        worstCaseButton.addActionListener(worstCaseListener);

        panel.add(new JLabel("Select List Type:"));
        panel.add(selector);
        panel.add(empiricalButton);
        panel.add(raceButton);
        panel.add(worstCaseButton);

        return panel;
    }

    public static JPanel createLoadingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel loadingLabel = new JLabel("Running tests, please wait...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(loadingLabel, BorderLayout.CENTER);
        return panel;
    }
}