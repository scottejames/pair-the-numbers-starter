import java.io.IOException;
import java.util.Arrays;

/** Demo runner -- runs findPair on one example case and prints the result. */
public class Main {
    public static void main(String[] args) throws IOException {
        TestData.TestCase c = TestData.loadCase(TestData.TEST_DATA_DIR + "/medium/01_trap_wrong_order_scan.txt");

        System.out.println("Case: " + c.name);
        System.out.println("Numbers: " + Arrays.toString(c.numbers));
        System.out.println("Target: " + c.target);

        int[] result = PairNumbers.findPair(c.numbers, c.target);
        System.out.println("Pair of indices: " + Arrays.toString(result));
    }
}
