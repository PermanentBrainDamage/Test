package com.yourcompany;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UIBuilder {

    public static JPanel createButtonPanel(ActionListener empiricalListener, ActionListener raceListener, ActionListener worstCaseListener) {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton empiricalButton = new JButton("Run Empirical Test (ArrayList)");
        empiricalButton.addActionListener(empiricalListener);

        JButton raceButton = new JButton("Run Race Test (LinkedList)");
        raceButton.addActionListener(raceListener);

        JButton worstCaseButton = new JButton("Show Worst-Case Complexity");
        worstCaseButton.addActionListener(worstCaseListener);

        buttonPanel.add(empiricalButton);
        buttonPanel.add(raceButton);
        buttonPanel.add(worstCaseButton);

        return buttonPanel;
    }

    public static JPanel createLoadingPanel() {
        JPanel loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.add(new JLabel("Running tests, please wait..."));
        return loadingPanel;
    }
}