import background.*;

public class Main {

    public static void solverTest(String filename) throws Exception {
        System.out.println(filename);
        Solver solver = new Solver();
        Cache cache = solver.processFile("build/resources/main/" + filename);

        Word[] words = cache.getRankedWords();
        for (Word word : words) {
            System.out.println(word);
        }
        System.out.println("Execution time: " + cache.getTime());
    }

    public static void test2() {
        Character c = null;
        System.out.println(c == null);
    }

    public static void main(String[] args) throws Exception {
        for (String filename : args)
            solverTest(filename);
    }
}
