package background;

import java.util.*;

/**
 * Cache
 *
 * This class stores important attributes of a processed text file.
 */
public class Cache {

    private long exeTime;
    private boolean timing;
    private Map<String, Word> map;
    // private Queue<Word> frequencyRanked;
    private TreeSet<Word> frequencyRanked;

    public Cache() {
        exeTime = -1l;
        timing = false;
        map = new HashMap<>();
        frequencyRanked = new TreeSet<>();
    }

    protected void update(String key) {
        Word word = (Word) (map.get(key));
        if (word == null) {
            word = new Word(key, 0);
            map.put(key, word);

        }

        word.incFreq();
        frequencyRanked.add(word);
        if (frequencyRanked.size() > 20)
            frequencyRanked.removeLast();
    }

    public Word get(String key) {
        return map.get(key);
    }

    public Queue<Word> getRankedWords() {
        return new PriorityQueue<>(frequencyRanked);
    }

    protected void startTimer() {
        timing = true;
        exeTime = System.currentTimeMillis();
    }

    protected void stopTimer() {
        exeTime = System.currentTimeMillis() - exeTime;
        timing = false;
    }

    public long getTime() {
        if (timing)
            return -1l;
        return exeTime;
    }
}
