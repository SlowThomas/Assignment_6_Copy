import background.*;

public class Main {

    /**
     * Pasrses the file as in the complete program. Outputs execution time to
     * stdout.
     *
     * @param filename filename of the text file, located in the resources folder
     * @throws Exception this is just a test so catch whatever exception it makes
     */
    public static void solverTest(String filename) throws Exception {
        System.out.println(filename);
        Solver solver = new Solver();
        Cache cache = solver.processFile("build/resources/main/" + filename);

        /*
         * Word[] words = cache.getRankedWords();
         *
         * for (Word word : words) {
         * System.out.println(word);
         * }
         */
        System.out.println("Execution time: " + cache.getTime() + "ms");
    }

    public static void main(String[] args) throws Exception {
        // For testing with command line arguments.
        for (String filename : args)
            solverTest(filename);

        // Explicit testing
        solverTest("MOBY.TXT");
        solverTest("ALICE.TXT");
    }
}
