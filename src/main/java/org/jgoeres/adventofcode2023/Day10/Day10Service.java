package org.jgoeres.adventofcode2023.Day10;

import org.jgoeres.adventofcode.common.Direction8Way;
import org.jgoeres.adventofcode.common.Utils.Pair;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.jgoeres.adventofcode.common.Direction8Way.*;
import static org.jgoeres.adventofcode2023.Day10.PipeType.*;

public class Day10Service {
    public boolean DEBUG = false;

    private Map<String, Pipe> pipes = new HashMap();
    private Pipe start;
    public static final char EMPTY = '.';
//    public static final char VERT = '|';
//    public static final char HORIZ = '-';
//    public static final char NORTH_EAST = 'L';
//    public static final char NORTH_WEST = 'J';
//    public static final char SOUTH_WEST = '7';
//    public static final char SOUTH_EAST = 'F';


    public Day10Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day10Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 10A ===");

        /**
         * Find the single giant loop starting at S.
         * How many steps along the loop does it take to get
         * from the starting position to the point farthest
         * from the starting position?
         **/

        // Start at the START pipe and go both directions.
        // See how far we can get before we run into each other
        long stepCount = 0;
        Set<Pipe> currentPipes = new HashSet();
        Set<Pipe> nextPipes = new HashSet();
        currentPipes.add(start);
        final HashSet<Pipe> visited = new HashSet();

        final Pair<Set<Pipe>> currentNextPair = new Pair(currentPipes, nextPipes);
        Boolean found = false;
        while (!found) {
            for (Pipe current : currentPipes) {
                // for each current pipe, find the connections we haven't already visited
                nextPipes.addAll(current.getConnections().values()
                        .stream().filter(c -> !visited.contains(c))
                        .collect(Collectors.toSet()));
                if (visited.containsAll(nextPipes)) {
                    // If we find a solution, flag it and don't iterate anymore
                    found = true;
                    break;
                }
            }
            if (!found) {
                stepCount++;    // increment the count
                visited.addAll(currentPipes);   // mark these pipes visited
                // Swap current for next
                currentNextPair.swap();
                currentPipes = currentNextPair.getFirst();
                nextPipes = currentNextPair.getSecond();
                nextPipes.clear();
            }
        }


        System.out.println("Day 10A: Answer = " + stepCount);
        return stepCount;
    }


    public long doPartB() {
        System.out.println("=== DAY 10B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 10B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        pipes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
            Pattern p = Pattern.compile("([FB]{7})([LR]{3})");
            Integer y = 0;
            while ((line = br.readLine()) != null) {
                // process the line.
                for (int x = 0; x < line.length(); x++) {
                    char pipeChar = line.charAt(x);
                    if (pipeChar != EMPTY) {
                        // First, just create the pipes but don't attach them
//
                        XYPoint xy = new XYPoint(x, y);
                        PipeType type = PipeType.fromChar(pipeChar);
                        Pipe newPipe = new Pipe(type, xy);
                        pipes.put(xy.toString(), newPipe);
                        if (type == START) start = newPipe;
                    }
                }
                y++;
            }
            // Now that we've got all the pipes, connect them up to each other
            int i = 0;
            for (Pipe pipe : pipes.values()) {
                i++;
                XYPoint xy = pipe.getXy();
//                System.out.printf("%d\t%s\n", i, xy);
                switch (pipe.getType()) {
                    case START:
                        // do nothing, the other connections will take care of the start
                        break;
                    case VERT:
                        pipe.addConnection(UP, pipes.get(xy.getRelativeLocation(UP).toString()));
                        pipe.addConnection(DOWN, pipes.get(xy.getRelativeLocation(DOWN).toString()));
                        break;
                    case HORIZ:
                        pipe.addConnection(LEFT, pipes.get(xy.getRelativeLocation(LEFT).toString()));
                        pipe.addConnection(RIGHT, pipes.get(xy.getRelativeLocation(RIGHT).toString()));
                        break;
                    case NORTH_EAST:
                        pipe.addConnection(UP, pipes.get(xy.getRelativeLocation(UP).toString()));
                        pipe.addConnection(RIGHT, pipes.get(xy.getRelativeLocation(RIGHT).toString()));
                        break;
                    case NORTH_WEST:
                        pipe.addConnection(UP, pipes.get(xy.getRelativeLocation(UP).toString()));
                        pipe.addConnection(LEFT, pipes.get(xy.getRelativeLocation(LEFT).toString()));
                        break;
                    case SOUTH_WEST:
                        pipe.addConnection(DOWN, pipes.get(xy.getRelativeLocation(DOWN).toString()));
                        pipe.addConnection(LEFT, pipes.get(xy.getRelativeLocation(LEFT).toString()));
                        break;
                    case SOUTH_EAST:
                        pipe.addConnection(DOWN, pipes.get(xy.getRelativeLocation(DOWN).toString()));
                        pipe.addConnection(RIGHT, pipes.get(xy.getRelativeLocation(RIGHT).toString()));
                        break;
                    default:
                        System.out.println("weird type:\t" + pipe.getXy());
                }
            }
            // Check for anything with too many connectios
            pipes.values().stream().filter(pipe -> pipe.getConnections().size() > 2).forEach(pipe -> System.out.printf("%s\t%d", pipe.getXy(), pipe.getConnections().size()));
        } catch (
                Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }


//    private void makePipeConnection(XYPoint xy, PipeType type) {
//        // Make a new pipe segment & connect it
//        XYPoint location = pipes.containsKey(xy) ? pipes.get(xy) :
//    }
}
