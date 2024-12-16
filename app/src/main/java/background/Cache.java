package background;

// Data Structures.
import java.util.*;

/**
 * Cache
 *
 * This class stores important attributes of a processed text file.
 */
public class Cache {

    // Attributes
    // The name of the file.
    private String filename;
    // The execution time.
    private long exeTime;
    // Whether or not timing is taking place.
    private boolean timing;
    // The map mapping each word to their corresponding frequencies.
    private Map<String, Integer> frequencyMap;
    // Stores the ranked words.
    private TreeSet<Word> frequencyRanked;
    // Content of the file.
    private String text;

    /**
     * Initialize the cache for the file.
     * 
     * @param filename the name of the file.
     */
    public Cache(String filename) {
        this.filename = filename;
        exeTime = -1l;
        timing = false;
        frequencyMap = new HashMap<>();
        frequencyRanked = new TreeSet<>();
    }

    // Public methods

    /**
     * Getter
     *
     * @return the file name.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Getter
     *
     * @return the content of the file.
     */
    public String getText() {
        return text;
    }

    /**
     * Getter
     *
     * @return the execution time, -1 if the timer is currently running.
     */
    public long getTime() {
        if (timing)
            return -1l;
        return exeTime;
    }

    /**
     * Rank the words for their frequencies.
     *
     * @return the words ranked.
     */
    public Word[] getRankedWords() {
        // Take advantage of TreeSet's property.
        Word[] ans = new Word[frequencyRanked.size()];
        int i = 0;
        for (Word word : frequencyRanked)
            ans[i++] = word;
        return ans;
    }

    // Package-private methods

    /**
     * Setter
     *
     * @param text the content of the file.
     */
    void setText(String text) {
        this.text = text;
    }

    /**
     * Update the frequency information for a word.
     * 
     * @param key the word to update.
     */
    void update(String key) {
        if (frequencyMap.containsKey(key)) {
            frequencyMap.put(key, frequencyMap.get(key) + 1);
        } else {
            frequencyMap.put(key, 1);
        }
    }

    /**
     * Rank the words for their frequencies.
     */
    void rank() {
        frequencyRanked = new TreeSet<>();
        // Go through all words.
        for (String key : frequencyMap.keySet()) {
            frequencyRanked.add(new Word(key, frequencyMap.get(key)));
            // Keep the list under 20 in cardinality.
            if (frequencyRanked.size() > 20)
                frequencyRanked.removeLast();
        }
    }

    /**
     * Starts the timer.
     */
    void startTimer() {
        timing = true;
        exeTime = System.currentTimeMillis();
    }

    /**
     * Stops the timer.
     */
    void stopTimer() {
        exeTime = System.currentTimeMillis() - exeTime;
        timing = false;
    }
}
