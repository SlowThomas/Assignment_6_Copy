package background;

/**
 * Word
 */
public class Word implements Comparable<Word> {
    protected String name;
    protected int frequency;

    public Word(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String toString() {
        return name + ": " + frequency;
    }

    @Override
    public int compareTo(Word nxt) {
        if (nxt.frequency != frequency)
            return nxt.frequency - frequency;
        return name.compareToIgnoreCase(nxt.name);
    }

    @Override
    public boolean equals(Object nxt) {
        return (nxt instanceof Word) && ((Word) nxt).name.equalsIgnoreCase(name) && ((Word) nxt).frequency == frequency;
    }
}
