import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loads Pair the Numbers test cases from ../test_data/{simple,medium,hard}/*.txt
 * (relative to the java/ directory -- the scripts in java/scripts/ always run
 * from there).
 *
 * File format:
 *
 *     name=&lt;case name&gt;
 *     target=&lt;long&gt;
 *     expected_i=&lt;int&gt;
 *     expected_j=&lt;int&gt;
 *     numbers=
 *     &lt;value 0&gt;
 *     &lt;value 1&gt;
 *     ...
 */
public class TestData {

    static final String TEST_DATA_DIR = "../test_data";

    static class TestCase {
        final String name;
        final int[] numbers;
        final long target;
        final int expectedI;
        final int expectedJ;

        TestCase(String name, int[] numbers, long target, int expectedI, int expectedJ) {
            this.name = name;
            this.numbers = numbers;
            this.target = target;
            this.expectedI = expectedI;
            this.expectedJ = expectedJ;
        }
    }

    static TestCase loadCase(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        String name = null;
        long target = 0;
        int expectedI = 0;
        int expectedJ = 0;
        int numbersStart = -1;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.equals("numbers=")) {
                numbersStart = i + 1;
                break;
            }
            int eq = line.indexOf('=');
            String key = line.substring(0, eq);
            String value = line.substring(eq + 1);
            if (key.equals("name")) {
                name = value;
            } else if (key.equals("target")) {
                target = Long.parseLong(value);
            } else if (key.equals("expected_i")) {
                expectedI = Integer.parseInt(value);
            } else if (key.equals("expected_j")) {
                expectedJ = Integer.parseInt(value);
            }
        }

        List<String> numberLines = lines.subList(numbersStart, lines.size());
        int[] numbers = new int[numberLines.size()];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(numberLines.get(i));
        }
        return new TestCase(name, numbers, target, expectedI, expectedJ);
    }

    static List<TestCase> loadTier(String tier) throws IOException {
        File dir = new File(TEST_DATA_DIR, tier);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        List<TestCase> cases = new ArrayList<>();
        if (files != null) {
            Arrays.sort(files);
            for (File f : files) {
                cases.add(loadCase(f.getPath()));
            }
        }
        return cases;
    }
}
