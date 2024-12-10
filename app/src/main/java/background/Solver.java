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
    private static Set<Character> separatorSet;
    private static Set<Character> punctuationSet;

    public Solver() {
        if (separatorSet == null) {
            separatorSet = new HashSet<>();
            // TODO: revise the separator set
            for (char c : new char[] { ' ', '\n', ',', '.', ':', ';', '<', '>', '/', '?', '!', '@', '#', '$',
                    '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '{', '}', '|', '\\', '\'', '\"', '~',
                    '`' })
                separatorSet.add(c);
        }
        if (punctuationSet == null) {
            punctuationSet = new HashSet<>();
            for (char c : new char[] { ' ', '\n', ',', '.', ':', ';', '?', '!', '`', '\'', '\"', '(', ')', '[', ']',
                    '{', '}' })
                punctuationSet.add(c);
        }
    }

    private boolean match(char character, char[] charSet) {
        for (char c : charSet)
            if (character == c)
                return true;
        return false;
    }

    private boolean isAlpha(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z');
    }

    private boolean isPunctuation(char c) {
        return punctuationSet.contains(c);
    }

    private boolean isDigit(char c) {
        return c <= '9' && c >= '0';
    }

    public boolean processFile(String filename) throws IOException {
        FileReader fr;
        try {
            fr = new FileReader(filename);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
            return false;
        }
        StringBuilder sb = new StringBuilder();
        char c = (char) fr.read();
        char nxt;

        while ((nxt = (char) fr.read()) != -1) {

            /*
             * [] don’t, isn’t, ... → one word
             * [] he’s, I’m, you’re, ... → one word
             * [] Shawn’s, apple’s, ... → remove ’s from words
             * [] Jonas’, ’twas , ... → remove beginning or ending apostrophes
             * [] ice-cream → one word (if chopped by line, change to two words)
             * [] 123, ... → either include ALL numbers or ignore ALL numbers
             * [] -, *, ... → ignore standalone symbols (or if a word begins/ends with it)
             */
            if (isAlpha(c) || isDigit(c)) {
                sb.append(c);
            } else {
                if (c == '\'') {
                    if (isAlpha(nxt) && sb.length() != 0)
                        sb.append(c);

                } else if (c == '-') {
                    // TODO:
                } else if (isPunctuation(c)) {
                } else {
                    if (sb.length() != 0 && !isPunctuation(nxt)) {
                        sb.append(c);
                    }
                }
            }

            c = nxt;
        }

        fr.close();
        return true;
    }
}
