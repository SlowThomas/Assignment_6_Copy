package background;

import java.io.*;
import java.util.*;

/**
 * Solver
 *
 * Solves the frequency and frequency ranking problem for a specific text file.
 */
public class Solver {

    private Map<String, Cache> map;
    private static Set<Character> symbolSet;
    private static Set<Character> punctuationSet;
    private static Set<Character> splitterSet;
    private static Set<String> contractionSet;
    private Cache cache;

    public Solver() throws IOException {
        /*
         * if (symbolSet == null) {
         * symbolSet = new HashSet<>();
         * // TODO: revise the separator set
         * for (char c : new char[] { ' ', '\n', ',', '.', ':', ';', '<', '>', '/', '?',
         * '!', '@', '#', '$',
         * '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '{', '}', '|',
         * '\\', '\'', '\"', '~',
         * '`' })
         * symbolSet.add(c);
         * }
         */
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
            contractionSet.add(line.substring(0, line.indexOf(',')));
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

    private void processWord(LinkedList<Character> string) {
        Character c;
        // Trim out beginning and ending symbols, including apostrophes
        while ((c = string.peekFirst()) != null && !isAlpha(c) && !isDigit(c))
            string.pollFirst();
        while ((c = string.peekLast()) != null && !isAlpha(c) && !isDigit(c))
            string.pollLast();

        if (string.size() == 0)
            return;

        StringBuilder sb = new StringBuilder(string.size());
        for (Character ch : string)
            sb.append(ch);
        if (contractionSet.contains(sb.toString())) {
            cache.update(sb.toString());
            return;
        }

        Character end = string.pollLast();
        if (string.peekLast() != null && string.peekLast() == '\'' && end == 's')
            string.pollLast();
        else
            string.addLast(end);

        sb = new StringBuilder(string.size());

        for (Character ch : string)
            sb.append(ch);
        cache.update(sb.toString());

    }

    public Cache processFile(String filename) throws IOException {
        FileReader fr;
        try {
            fr = new FileReader(filename);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            return null;
        }
        cache = new Cache();
        cache.startTimer();
        LinkedList<Character> buffer = new LinkedList<>();
        char c;

        while ((c = (char) fr.read()) != -1) {

            /*
             * [v] 123, ... → include ALL numbers
             * [v] in the contraction set → one word
             * [v] Shawn’s, apple’s, ... → remove ’s from words
             * [v] Jonas’, ’twas , ... → remove beginning or ending apostrophes
             * [v] keep symbols in the middle of the word.
             * [v] if a word is chopped by line by a symbol, change to two words
             * [v] -, *, ... → ignore standalone symbols (or if a word begins/ends with it)
             * TODO: Question: can I treat all symbols as splitters?
             */
            if (isSplitter(c)) {
                if (buffer.size() != 0) {
                    processWord(buffer);
                    buffer = new LinkedList<>();
                }
            } else
                buffer.addLast(c);
            System.out.println(c);
        }
        if (buffer.size() != 0)
            processWord(buffer);

        fr.close();
        cache.stopTimer();
        return cache;
    }
}
