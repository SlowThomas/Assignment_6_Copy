package ui;

import java.awt.*;
import javax.swing.*;

public class UI {

    public UI() {
        // Feature panels

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new GridBagLayout());

        JFrame frame = new JFrame("Assignment 6");
        frame.setSize(new Dimension(500, 500));
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
