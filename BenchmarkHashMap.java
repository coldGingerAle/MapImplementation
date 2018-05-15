import java.io.File;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class BenchmarkHashMap {
    // Configurable number of increments and their size
    private static final int NUM_INCREMENTS = 10;
    private static final int DEFAULT_INCREMENT_SIZE = 100000;

    private static final Random r = new Random();

    // Do not instantiate
    private BenchmarkHashMap() { }

    private static long benchmarkPut(Map<String, Integer> map, int n) {
        // Generate the test data before running the timer.
        String[] stringArray = Utilities.generateStrings(n);
        Integer[] integerArray = Utilities.generateIntegers(n);

        // Start the timer once the test data is ready to be inserted into the map.
        Utilities.startTimer();

        // Put the test data into the map.
        for (int i = 0; i < n; i++) {
            map.put(stringArray[i], integerArray[i]);
        }

        if (map.size() != n) {
            System.out.println("Debug: something is wrong.");
        }

        // Stop the timer and return the milliseconds it took to put all data.
        return Utilities.elapsedTime();
    }

    private static long benchmarkRemove(Map<String, Integer> map, int n) {
        // Generate the test data before running the timer.
        String[] stringArray = Utilities.generateStrings(n);
        Integer[] integerArray = Utilities.generateIntegers(n);

        // Put the entries into the map before starting timer for removal
        for (int i = 0; i < n; i++) {
            map.put(stringArray[i], integerArray[i]);
        }

        if (map.size() != n) {
            System.out.println("Debug: something is wrong.");
        }

        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            stringList.add(stringArray[i]);
        }
        Collections.shuffle(stringList); // Shuffle strings so elements can be removed in random order

        // Start the timer once the entries are ready to be removed
        Utilities.startTimer();

        for (int i = 0; i < n; i++) {
            map.remove(stringList.get(i));
        }

        // Stop the timer and return the nanoseconds it took to remove all data.
        return Utilities.elapsedTime();
    }

    // General benchmark function that allows the caller to specify the type and
    // the implementation of the benchmark, as well as the amount of data to use.
    private static long benchmark(String impl, String type, int n) {
        // Instantiate a map using an appropriate implementation
        Map<String, Integer> map;
        switch (impl) {
            case "linked_map":
                map = new LinkedMap<>();
                break;
            case "hash_map":
                map = new HashMap<>();
                break;
            case "bst_map":
                map = new BSTMap<>();
                break;
            default:
                throw new InvalidParameterException(
                        "Invalid map implementation chosen: " + impl);
        }

        // Run the appropriate benchmark.
        switch (type) {
            case "put":
                return benchmarkPut(map, n);
            case "remove":
                return benchmarkRemove(map, n);
            default:
                throw new InvalidParameterException(
                        "Invalid benchmark type chosen: " + type);
        }
    }

    // Perform a set of benchmarks and write the results to file.
    private static void benchmarkAndPrintResultsToFile(String impl,String type, int incrementSize) {
        int[] sizes = new int[NUM_INCREMENTS];
        double[] times = new double[NUM_INCREMENTS];

        // Perform the tests for each increment (i.e. several increasingly larger
        // amounts of data).
        for (int i = 0; i < NUM_INCREMENTS; i++) {
            // Calculate the amount of data for this increment.
            int n = i * incrementSize;

            // Run each benchmark 10 times
            long total_time_elapsed = 0;
            for (int j = 0; j < 10; j++) {
                total_time_elapsed += benchmark(impl, type, n);
            }

            // Record the size of data set used and the amount of time it took.
            sizes[i] = n;
            times[i] = (total_time_elapsed / 10d);
            System.out.println(impl + ": " + n + " items, " + times[i] + " ms avg.");
        }

        // Create a file named appropriately to represent the benchmark.
        File f = new File("results/" + impl + "_" + type + ".csv");
        try {
            // Create the "results/" directory if necessary.
            f.mkdirs();

            // Delete the file if it already exists (to write only new results).
            if (f.exists()) {
                f.delete();
            }

            // Create a new writer to the file.
            PrintWriter pw = new PrintWriter(f);

            // Print the obtained data.
            for (int i = 0; i < NUM_INCREMENTS; i++) {
                pw.println(sizes[i] + "," + times[i]);
            }

            // Flush and close the writer.
            pw.flush();
            pw.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // Convenience function to print information to the user of benchmark tool.
    private static void printUsage() {
        System.out.println("Usage: BenchmarkHashMap <implementation> <benchmark>");
        System.out.println("  where:");
        System.out.println("    * <implementation> could be:");
        System.out.println("       * linked_map");
        System.out.println("       * hash_map");
        System.out.println("       * bst_map");
        System.out.println("    * <benchmark> could be:");
        System.out.println("       * put");
        System.out.println("       * remove");
    }

    // The main entry point of the benchmark program.
    public static void main(String[] args) {
        // Ensure there are two arguments passed; otherwise, print instructions.
        if (args.length < 2) {
            printUsage();
            return;
        }

        // Run the benchmark suite.
        try {
            String impl = args[0];
            String type = args[1];

            int incrementSize = DEFAULT_INCREMENT_SIZE;
            if (args.length == 3) {
                incrementSize = Integer.parseInt(args[2]);
            }

            benchmarkAndPrintResultsToFile(impl, type, incrementSize);
        } catch (InvalidParameterException ipe) {
            System.out.println("Error: " + ipe.getMessage());
            printUsage();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
