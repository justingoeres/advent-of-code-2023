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

        System.out.println("Day 8A: Steps for me = " + steps);
        return steps;
    }

    public long doPartB() {
        System.out.println("=== DAY 8B ===");

        long result = 0;
        /**
         * Simultaneously start on every node that ends with A.
         * How many steps does it take before you're only on nodes that end with Z?
         **/

        // Start at all nodes ending in 'A'
        final Set<Node> ghostNodes = nodes.entrySet().stream()
                .filter(nodeEntry -> nodeEntry.getKey().charAt(2) == 'A')
                .map(nodeEntry -> nodeEntry.getValue())
                .collect(Collectors.toSet());

        final Set<Node> targetNodes = nodes.entrySet().stream()
                .filter(nodeEntry -> nodeEntry.getKey().charAt(2) == 'Z')
                .map(nodeEntry -> nodeEntry.getValue())
                .collect(Collectors.toSet());

        List<Long> ghostSteps = new ArrayList<>();

        // Calculate the number of steps for each ghost individually
        for (Node ghostNode : ghostNodes) {
            long steps = 0;
            int i = 0;
            while (!(targetNodes.contains(ghostNode))) {
                final char nextStep = directions[i];
                // move to the next node
                ghostNode = nodes.get((nextStep == LEFT) ? ghostNode.getLeft() : ghostNode.getRight());
                steps++;    // increment our step count
                i = (i + 1) % directions.length;    // increment the directions pointer, with rollover
            }
            // Now that we know how many steps for this ghost, record it
            ghostSteps.add(steps);
        }
        // Now that we know all the steps, the least common multiple of all of them is when the ghosts will be home together
        result = ghostSteps.stream().reduce(1L, (a, b) -> lcm(a, b));
        System.out.println("Day 8B: Steps for ghosts = " + result);
        return result;
    }

    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
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
