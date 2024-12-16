package background;

// IO Dependency
import java.io.*;

// Data Structure.
import java.util.*;

/**
 * Solver
 *
 * Solves the frequency and frequency ranking problem for a specific text file.
 *
 * Word extraction follows following rules:
 * > 123, ... → include ALL numbers
 * > in the contraction set → one word
 * > Shawn’s, apple***’s, ... → remove ’s from words
 * > Jonas’, ’twas , ... → remove beginning or ending apostrophes
 * > keep symbols in the middle of the word.
 * > if a word is chopped by line by a symbol, change to two words
 * > -, *, ... → ignore standalone symbols (or if a word begins/ends with it)
 */
public class Solver {

    // Definite separators.
    private static Set<Character> splitterSet;
    // Word contractions.
    private static Set<String> contractionSet;
    // The currently processing file's cache.
    private Cache cache;

    /**
     * Load sets of elements for reading.
     * 
     * @throws IOException
     */
    public Solver() throws IOException {
        // Define the definite splitters.
        if (splitterSet == null) {
            splitterSet = new HashSet<>();
            for (char c : new char[] { ' ', '\n', ',', '.', ':', ';', '?', '!', '`', '\"', '(', ')', '[', ']',
                    '{', '}' })
                splitterSet.add(c);
        }

        // Define the word contractions.
        contractionSet = new HashSet<>();
        BufferedReader fin = new BufferedReader(new FileReader("build/resources/main/contractions.txt"));
        String line;
        while ((line = fin.readLine()) != null) {
            contractionSet.add(line.substring(0, line.indexOf(',')).toLowerCase());
        }
        fin.close();
    }

    /**
     * Checks if the current character is a definite splitter.
     * 
     * @param c The character.
     * @return Whether or not the character is a definite splitter.
     */
    private boolean isSplitter(char c) {
        return splitterSet.contains(c);
    }

    /**
     * Chacks if the current character is a definite valid character.
     * 
     * @param c The character.
     * @return Whether or not the character is a definite valid character.
     */
    private boolean isValidChar(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9');
    }

    /**
     * Trim out symbols from both ends of the string.
     *
     * @param string The string.
     * @return The modified string.
     */
    private String trimSymbols(String string) {
        int i = 0;
        int j = string.length() - 1;
        // Trim out beginning and ending symbols, including apostrophes
        while (i < string.length() && !isValidChar(string.charAt(i)))
            i++;
        while (j >= i && !isValidChar(string.charAt(j)))
            j--;

        return string.substring(i, j + 1);
    }

    // The main string builder.
    StringBuilder sb = new StringBuilder();
    // Parts separated by single quotation marks.
    ArrayList<String> parts = new ArrayList<>();

    /**
     * Process a single string free of definite splitters and load them to the
     * cache.
     * 
     * @param string The string.
     */
    private void processWord(StringBuilder string) {
        // Skip for empty strings.
        if (string.length() == 0)
            return;

        // Split the string with single quotation marks.
        sb.delete(0, sb.length());
        parts.clear();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '\'') {
                parts.add(sb.toString());
                sb.delete(0, sb.length());
            } else
                sb.append(string.charAt(i));
        }
        if (sb.length() != 0)
            parts.add(sb.toString());

        // Connect adjacent parts if appropriate; load into the cache.
        for (int i = 0; i < parts.size(); i++) {
            if (i == parts.size() - 1) {
                // The last part is not merged: load directly.
                String temp = trimSymbols(parts.get(i));
                if (temp.length() > 0)
                    cache.update(temp);
            } else {
                String temp = trimSymbols(parts.get(i) + '\'' + parts.get(i + 1));
                if (contractionSet.contains(temp)) {
                    // Load the merged string if it matches a word contraction.
                    cache.update(temp);
                    i++;
                } else if (temp.length() > 1 && temp.charAt(temp.length() - 2) == '\''
                        && temp.charAt(temp.length() - 1) == 's') {
                    // Trim out "'s" if the word ends with it.
                    if (temp.length() > 2)
                        cache.update(temp.substring(0, temp.length() - 2));
                    i++;
                } else {
                    // Update the unmerged string.
                    temp = trimSymbols(parts.get(i));
                    if (temp.length() > 0)
                        cache.update(temp);
                }
            }
        }
    }

    /**
     * Process the whole file, load contents into the cache.
     * 
     * @param filename the name of the file, containing the complete path, to
     *                 process.
     * @return the updated cache object for the file.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Cache processFile(String filename) throws FileNotFoundException, IOException {
        // Initialize the cache.
        cache = new Cache(filename);
        // Start the timer.
        cache.startTimer();
        // Initialize the file reader.
        FileReader fin = new FileReader(filename);

        // Store plain copy of file contents.
        StringBuilder completeText = new StringBuilder();
        for (int c; (c = fin.read()) != -1; completeText.append((char) c))
            ;
        fin.close();
        cache.setText(completeText.toString());

        // A continuous string of character free of definite splitters.
        StringBuilder buffer = new StringBuilder();
        // Go through file contents.
        for (int i = 0; i < completeText.length(); i++) {
            // Load the current character.
            char c = completeText.charAt(i);

            if (isSplitter(c)) {
                // When encountering a definite splitter, process the current buffer.
                if (buffer.length() != 0) {
                    processWord(buffer);
                    // Clear the buffer.
                    buffer.delete(0, buffer.length());
                }
            } else {
                // Make alphabetical characters lowercase.
                if ('A' <= c && c <= 'Z')
                    c += 32;
                // Add to the buffer.
                buffer.append(c);
            }
        }
        if (buffer.length() != 0)
            processWord(buffer);

        // Rank the words for their frequencies.
        cache.rank();
        // Stop the timer.
        cache.stopTimer();

        return cache;
    }
}
