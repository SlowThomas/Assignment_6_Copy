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
    private Map<String, Integer> frequencyMap;
    private TreeSet<Word> frequencyRanked;

    public Cache() {
        exeTime = -1l;
        timing = false;
        frequencyMap = new HashMap<>();
        frequencyRanked = new TreeSet<>();
    }

    protected void update(String key) {
        if (frequencyMap.containsKey(key)) {
            frequencyMap.put(key, frequencyMap.get(key) + 1);
        } else {
            frequencyMap.put(key, 1);
        }
    }

    protected void rank() {
        frequencyRanked = new TreeSet<>();
        for (String key : frequencyMap.keySet()) {
            frequencyRanked.add(new Word(key, frequencyMap.get(key)));
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
