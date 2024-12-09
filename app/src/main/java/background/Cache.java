package background;

import java.util.*;

/**
 * Cache
 *
 * This class stores important attributes of a processed text file.
 */
class Cache {

    protected int exeTime;
    protected Map<String, Word> map;
    protected Queue<Word> frequencyRanked;

    public Cache() {
        exeTime = 0;
        map = new HashMap<>();
        frequencyRanked = new PriorityQueue<>();
    }

    protected void update(String key) {
        Word word = (Word) (map.get(key));

        word.incFreq();
        frequencyRanked.add(word);
        if (frequencyRanked.size() > 20)
            frequencyRanked.poll();
    }
}
