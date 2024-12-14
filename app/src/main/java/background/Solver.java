package background;

import java.io.*;
import java.util.*;

/**
 * Solver
 *
 * Solves the frequency and frequency ranking problem for a specific text file.
 *
 * Word extraction follows following rules:
 * > 123, ... → include ALL numbers
 * > in the contraction set → one word
 * > Shawn’s, apple’s, ... → remove ’s from words
 * > Jonas’, ’twas , ... → remove beginning or ending apostrophes
 * > keep symbols in the middle of the word.
 * > if a word is chopped by line by a symbol, change to two words
 * > -, *, ... → ignore standalone symbols (or if a word begins/ends with it)
 */
public class Solver {

    private static Set<Character> splitterSet;
    private static Set<String> contractionSet;
    private Cache cache;

    public Solver() throws IOException {
        if (splitterSet == null) {
            splitterSet = new HashSet<>();
            for (char c : new char[] { ' ', '\n', ',', '.', ':', ';', '?', '!', '`', '\"', '(', ')', '[', ']',
                    '{', '}' })
                splitterSet.add(c);
        }

        contractionSet = new HashSet<>();
        BufferedReader fin = new BufferedReader(new FileReader("build/resources/main/contractions.txt"));
        String line;

        while ((line = fin.readLine()) != null) {
            contractionSet.add(line.substring(0, line.indexOf(',')).toLowerCase());
        }
        fin.close();

    }

    private boolean isSplitter(char c) {
        return splitterSet.contains(c);
    }

    private boolean isValidChar(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || ('0' <= c && c <= '9');
    }

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

    StringBuilder sb = new StringBuilder();
    ArrayList<String> parts = new ArrayList<>();

    private void processWord(StringBuilder string) {
        if (string.length() == 0)
            return;

        // Split the string with '
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

        for (int i = 0; i < parts.size(); i++) {
            if (i == parts.size() - 1) {
                String temp = trimSymbols(parts.get(i));
                if (temp.length() > 0)
                    cache.update(temp);
            } else {
                String temp = trimSymbols(parts.get(i) + '\'' + parts.get(i + 1));
                if (contractionSet.contains(temp)) {
                    cache.update(temp);
                    i++;
                } else if (temp.length() > 1 && temp.charAt(temp.length() - 2) == '\''
                        && temp.charAt(temp.length() - 1) == 's') {
                    if (temp.length() > 2)
                        cache.update(temp.substring(0, temp.length() - 2));
                    i++;
                } else {
                    temp = trimSymbols(parts.get(i));
                    if (temp.length() > 0)
                        cache.update(temp);
                }
            }
        }

    }

    public Cache processFile(String filename) throws IOException {
        cache.startTimer();
        FileReader fin;
        try {
            fin = new FileReader(filename);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            return null;
        }
        cache = new Cache();
        StringBuilder buffer = new StringBuilder();
        int c;

        while ((c = fin.read()) != -1) {
            if (isSplitter((char) c)) {
                if (buffer.length() != 0) {
                    processWord(buffer);
                    // buffer.clear();
                    buffer.delete(0, buffer.length());
                }
            } else {
                if ('A' <= c && c <= 'Z')
                    c += 32;
                buffer.append((char) c);
            }
        }
        if (buffer.length() != 0)
            processWord(buffer);

        fin.close();
        cache.rank();
        cache.stopTimer();

        return cache;
    }
}
