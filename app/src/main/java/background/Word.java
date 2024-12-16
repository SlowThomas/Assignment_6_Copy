package background;

/**
 * Word
 *
 * The class describing a word's identity and frequency in its domain.
 */
public class Word implements Comparable<Word> {
    // Key Attributes
    protected String name;
    protected int frequency;

    /**
     * Initialize the instance.
     * 
     * @param name      The text itself.
     * @param frequency The frequency of this word.
     */
    public Word(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    /**
     * Getter.
     * 
     * @return The text.
     */
    public String getText() {
        return name;
    }

    /**
     * Gatter.
     *
     * @return The frequency.
     */
    public int getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return name + ": " + frequency;
    }

    @Override
    public int compareTo(Word nxt) {
        // Sort first according to frequency, then name alphabetically.
        if (nxt.frequency != frequency)
            return nxt.frequency - frequency;
        return name.compareToIgnoreCase(nxt.name);
    }

    @Override
    public boolean equals(Object nxt) {
        // Check all attributes.
        return (nxt instanceof Word) && ((Word) nxt).name.equalsIgnoreCase(name) && ((Word) nxt).frequency == frequency;
    }
}
