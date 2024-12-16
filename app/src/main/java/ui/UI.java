package ui;

// Data Structures
import java.util.*;

// IO Dependency
import java.io.IOException;

// GUI Dependencies
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Connect to background processes.
import background.*;

/**
 * UI
 *
 * The main handler creating dynamic graphical elements and user interaction.
 */
public class UI implements ActionListener {

    // Components of the rough layout.
    // Names are self-explanatory.
    private JFrame frame;
    private JPanel mainPanel;
    private LeftPanel leftPanel;
    private RightPanel rightPanel;

    // Background Process Data
    private HashMap<String, Cache> caches;
    private Solver solver;

    /**
     * Initializes the user interface.
     *
     * @throws IOException
     */
    public UI() throws IOException {
        // Initialize Background Process Data.
        caches = new HashMap<>();
        solver = new Solver();

        // Preload ALICE.TXT and MOBY.TXT.
        String dir = "build/resources/main/";
        caches.put("ALICE.TXT", solver.processFile(dir + "ALICE.TXT"));
        caches.put("MOBY.TXT", solver.processFile(dir + "MOBY.TXT"));

        // Initialize the components.
        frame = new JFrame("Assignment 6");
        mainPanel = new JPanel();
        leftPanel = new LeftPanel();
        rightPanel = new RightPanel();

        // Setup the frame.
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup the components.
        leftPanel.addFile.addActionListener(this);
        leftPanel.fileSelection.addActionListener(this);
        for (String key : caches.keySet())
            leftPanel.fileSelection.addItem(key);

        // Attach the components.
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weighty = 1d;
        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(rightPanel, gbc);

        // Attach the main panel and display the interface.
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Fetch the event source.
        Object source = e.getSource();
        if (source == leftPanel.addFile) {
            // Open a file dialog to request for a new file to read.
            FileDialog openDialog = new FileDialog(frame, "Open a new file", FileDialog.LOAD);
            openDialog.setVisible(true);
            String filename = openDialog.getFile();
            String dir = openDialog.getDirectory();
            if (filename == null || dir == null)
                // In the case where no file is selected, cancel the operation.
                return;
            try {
                // Add the file
                // For duplicate filenames, keep the newest file.
                if (!caches.containsKey(filename)) {
                    caches.put(filename, solver.processFile(dir + filename));
                    leftPanel.fileSelection.addItem(filename);
                } else {
                    caches.put(filename, solver.processFile(dir + filename));
                }
            } catch (IOException err) {
                // Open a dialog to warn against corrupted files.
                JDialog dialog = new JDialog();
                dialog.add(new JLabel(err.getMessage()));
                dialog.setSize(500, 200);
                dialog.setLocationByPlatform(true);
                dialog.setVisible(true);
            }
        } else if (source == leftPanel.fileSelection) {
            // Fetch the corresponding file cache.
            Cache cache = caches.get(leftPanel.fileSelection.getSelectedItem());

            // Filter out null input.
            if (cache != null) {
                // Update the dinamic components to reflect the change.
                leftPanel.previewContent.setText(cache.getText());
                rightPanel.content.setText(
                        String.format("Total Time: %d miliseconds\n\n20 Most Frequent Words\n\n%4s%-16sFrequency\n\n",
                                cache.getTime(), "", "Words"));
                Word[] words = cache.getRankedWords();
                for (int i = 0; i < 20; i++) {
                    if (i < words.length)
                        rightPanel.content.append(
                                String.format("%-4s%-16s%d\n", i + 1 + ")",
                                        words[i].getText(),
                                        words[i].getFrequency()));
                    else
                        rightPanel.content.append(i + 1 + ")\n");
                }
                // Scroll the scrollPanes to the top.
                leftPanel.previewContent.setCaretPosition(0);
                rightPanel.content.setCaretPosition(0);
            }
        }
    }
}
