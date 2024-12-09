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

    public Solver() {
        if (separatorSet == null) {
            separatorSet = new HashSet<>();
            // TODO: revise the separator set
            for (char c : new char[] { ' ', '\n', ',', '.', ':', ';', '<', '>', '/', '?', '!', '@', '#', '$',
                    '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '{', '}', '|', '\\', '\'', '\"', '~',
                    '`' })
                separatorSet.add(c);
        }
    }

    private boolean match(char character, char[] charSet) {
        for (char c : charSet)
            if (character == c)
                return true;
        return false;
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
        char c;

        while ((c = (char) fr.read()) != -1) {
            if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
                sb.append(c);
            } else if (c == '\'') {
            } else if (c == '-') {
            } else {
            }
        }

        fr.close();
        return true;
    }
}
