package org.jgoeres.adventofcode2023.Day08;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day08Service {
    public boolean DEBUG = false;

    private char[] directions;
    private Map<String, Node> nodes = new HashMap();
    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';

    public Day08Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day08Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 8A ===");

        /**
         * Starting at AAA, follow the left/right instructions.
         * How many steps are required to reach ZZZ?
         **/

        long steps = 0;
        int i = 0; // the index of the directions array we're at
        Node currentNode = nodes.get("AAA");    // start at AAA
        final Node targetNode = nodes.get("ZZZ");
        while (!(currentNode == targetNode)) {
            final char nextStep = directions[i];
            // move to the next node
            currentNode = nodes.get((nextStep == LEFT) ? currentNode.getLeft() : currentNode.getRight());
            steps++;    // increment our step count
            i = (i + 1) % directions.length;    // increment the directions pointer, with rollover
        }

        System.out.println("Day 8A: Answer = " + steps);
        return steps;
    }

    public long doPartB() {
        System.out.println("=== DAY 8B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 8B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        nodes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            /** Replace this regex **/
            String line = br.readLine();    // just read the first line
            directions = line.toCharArray();
            // NFK = (LMH, RSS)
            // SLJ = (NBT, CDG)
            // SKX = (SRC, KKX)
            Pattern p = Pattern.compile("(...) = \\((...), (...)\\)");
            while ((line = br.readLine()) != null) {
                // process the line.
                Matcher m = p.matcher(line);
                if (m.find()) { // If our regex matched this line
                    // Parse it
                    String name = m.group(1);
                    String left = m.group(2);
                    String right = m.group(3);
                    nodes.put(name, new Node(left, right));
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
