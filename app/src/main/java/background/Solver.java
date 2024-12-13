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

    private boolean isAlpha(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z');
    }

    private boolean isDigit(char c) {
        return c <= '9' && c >= '0';
    }

    private String trimSymbols(String string) {
        int i = 0;
        int j = string.length() - 1;
        // Trim out beginning and ending symbols, including apostrophes
        while (i < string.length() && !isAlpha(string.charAt(i)) && !isDigit(string.charAt(i)))
            i++;
        while (j >= i && !isAlpha(string.charAt(j)) && !isDigit(string.charAt(j)))
            j--;

        return string.substring(i, j + 1);
    }

    private void processWord(LinkedList<Character> string) {
        if (string.size() == 0)
            return;

        // Split the string with '
        StringBuilder sb = new StringBuilder();
        LinkedList<String> parts = new LinkedList<>();
        for (Character c : string) {
            if (c == '\'') {
                parts.add(sb.toString());
                sb = new StringBuilder();
            } else
                sb.append(c);
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
                } else if (temp.endsWith("\'s")) {
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
        FileReader fin;
        try {
            fin = new FileReader(filename);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            return null;
        }
        cache = new Cache();
        cache.startTimer();
        LinkedList<Character> buffer = new LinkedList<>();
        int c;

        while ((c = fin.read()) != -1) {
            if (isSplitter((char) c)) {
                if (buffer.size() != 0) {
                    processWord(buffer);
                    buffer = new LinkedList<>();
                }
            } else {
                if ('A' <= c && c <= 'Z')
                    c += 32;
                buffer.addLast((char) c);
            }
        }
        if (buffer.size() != 0)
            processWord(buffer);

        fin.close();
        cache.rank();
        cache.stopTimer();
        return cache;
    }
}
