package org.jgoeres.adventofcode${AOC_YEAR}.Day${AOC_DAY};
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day${AOC_DAY}Service {
    public boolean DEBUG = false;

    private ArrayList<Integer> inputList = new ArrayList<>();

    public Day${AOC_DAY}Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day${AOC_DAY}Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY ${AOC_PRETTY_DAY}A ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day ${AOC_PRETTY_DAY}A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY ${AOC_PRETTY_DAY}B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day ${AOC_PRETTY_DAY}B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and apply a regex to extract fields
    private void loadInputs(String pathToFile) {
        inputList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            Integer nextInt = 0;
            /** Replace this regex **/
            Pattern p = Pattern.compile("([FB]{7})([LR]{3})");
            while ((line = br.readLine()) != null) {
                // process the line.
                Matcher m = p.matcher(line);
                if (m.find()) { // If our regex matched this line
                    // Parse it
                    String field1 = m.group(1);
                    String field2 = m.group(2);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
