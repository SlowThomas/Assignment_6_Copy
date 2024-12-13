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
        // TODO: Ignore case
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
