package ui;

// GUI Dependencies.
import java.awt.*;
import javax.swing.*;

/**
 * RightPanel
 *
 * Panel on the right.
 */
class RightPanel extends JPanel {

    // Package-private components
    // Names are self-explanatory.
    JScrollPane wrapper;
    JTextArea content;

    /**
     * Generates the layout of the right panel.
     */
    public RightPanel() {
        // Set the main layout.
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1d;
        gbc.weighty = 1d;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Setup the components.
        content = new JTextArea();
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setFont(new Font("Courier", Font.PLAIN, 12));
        content.setMargin(new Insets(20, 20, 20, 20));
        content.setEditable(false);

        // Attach the components.
        wrapper = new JScrollPane(content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(wrapper, gbc);
    }
}
