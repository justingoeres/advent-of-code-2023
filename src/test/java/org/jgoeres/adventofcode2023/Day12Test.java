package org.jgoeres.adventofcode2023;

import org.jgoeres.adventofcode2023.Day12.Day12Service;
import org.jgoeres.adventofcode.common.ToClipboard;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Day12Test {
    // Puzzle
    private final String PUZZLE_INPUT = "data/day12/input.txt";
    private final boolean PUZZLE_DEBUG = false;
    private Day12Service day12Service = null;

    // Examples
    private final String EXAMPLE1_INPUT = "data/day12/example1.txt";
    private final boolean EXAMPLE_DEBUG = false;
    private Day12Service example1Service = null;

    @Test
    @Order(1)   // Run before Puzzle Part B
    public void Day12A() {
        if (day12Service == null) {
            day12Service = new Day12Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 7251;
        long result = 0;
        try {
            result = day12Service.doPartA();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(2)   // Run after Puzzle Part A
    public void Day12B() {
        if (day12Service == null) {
            day12Service = new Day12Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = day12Service.doPartB();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(3)   // Run before Example Part B
    @Disabled
    public void Day12AExample1() {
        example1Service = new Day12Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);
        final long EXPECTED = 21;
        long result = 0;
        try {
            result = example1Service.doPartA();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(4)   // Run after Example Part A
    @Disabled
    public void Day12BExample1() {
        // Instantiate the service if Part A was skipped
        if (example1Service == null) example1Service = new Day12Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = example1Service.doPartB();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }
}
