package background;

/**
 * Word
 */
class Word implements Comparable<Word> {
    protected String name;
    protected int frequency;

    public Word(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public void incFreq() {
        frequency++;
    }

    public String toString() {
        return name + ": " + frequency;
    }

    @Override
    public int compareTo(Word nxt) {
        return nxt.frequency - frequency;
    }

    @Override
    public boolean equals(Object nxt) {
        return nxt instanceof Word && ((Word) nxt).name.equals(name);
    }
}
