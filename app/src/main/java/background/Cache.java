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
    private Map<String, Integer> map;
    private TreeSet<Word> frequencyRanked;

    public Cache() {
        exeTime = -1l;
        timing = false;
        map = new HashMap<>();
        frequencyRanked = new TreeSet<>();
    }

    protected void update(String key) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
    }

    protected void rank() {
        frequencyRanked = new TreeSet<>();
        for (String key : map.keySet()) {
            frequencyRanked.add(new Word(key, map.get(key)));
            if (frequencyRanked.size() > 20)
                frequencyRanked.removeLast();
        }
    }

    public Word[] getRankedWords() {
        Word[] ans = new Word[frequencyRanked.size()];
        int i = 0;
        for (Word word : frequencyRanked)
            ans[i++] = word;
        return ans;
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
