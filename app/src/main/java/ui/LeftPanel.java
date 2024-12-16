package ui;

// GUI Dependencies.
import java.awt.*;
import javax.swing.*;

/**
 * LeftPanel
 *
 * The panel on the left.
 */
class LeftPanel extends JPanel {

    // Package-private components
    // Names are self-explanatory.
    JButton addFile;
    JComboBox<String> fileSelection;
    JScrollPane previewWrapper;
    JTextArea previewContent;

    /**
     * Generate the left panel layout.
     */
    public LeftPanel() {
        // Set the main layout.
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1d;
        gbc.gridx = 0;

        // Setup and attach the components
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addFile = new JButton("Add File");
        add(addFile, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fileSelection = new JComboBox<>();
        add(fileSelection, gbc);

        previewContent = new JTextArea();
        previewContent.setFont(new Font("Courier", Font.PLAIN, 12));
        previewContent.setMargin(new Insets(20, 20, 20, 20));
        previewContent.setEditable(false);

        gbc.gridy = 2;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        previewWrapper = new JScrollPane(previewContent,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(previewWrapper, gbc);
    }
}
