import background.*;

public class Main {
    public static void test1() throws Exception {

        System.out.println("Main");
        Solver solver = new Solver();
        Cache cache = solver.processFile("build/resources/main/test.txt");

        System.out.println(cache.getRankedWords());

    }

    public static void test2() {
        Character c = null;
        System.out.println(c == null);
    }

    public static void main(String[] args) throws Exception {
        test1();
    }
}
