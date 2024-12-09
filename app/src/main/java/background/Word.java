package background;

/**
 * Word
 */
class Word {
    protected String name;
    protected int frequency;

    public Word(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public void incFreq() {
        frequency++;
    }
}
