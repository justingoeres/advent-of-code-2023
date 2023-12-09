package org.jgoeres.adventofcode2023;

import org.jgoeres.adventofcode2023.Day09.Day09Service;
import org.jgoeres.adventofcode.common.ToClipboard;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Day09Test {
    // Puzzle
    private final String PUZZLE_INPUT = "data/day09/input.txt";
    private final boolean PUZZLE_DEBUG = false;
    private Day09Service day09Service = null;

    // Examples
    private final String EXAMPLE1_INPUT = "data/day09/example1.txt";
    private final boolean EXAMPLE_DEBUG = false;
    private Day09Service example1Service = null;

    @Test
    @Order(1)   // Run before Puzzle Part B
    public void Day09A() {
        if (day09Service == null) {
            day09Service = new Day09Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = day09Service.doPartA();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(2)   // Run after Puzzle Part A
    public void Day09B() {
        if (day09Service == null) {
            day09Service = new Day09Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = day09Service.doPartB();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(3)   // Run before Example Part B
    @Disabled
    public void Day09AExample1() {
        example1Service = new Day09Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);
        final long EXPECTED = 0;
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
    public void Day09BExample1() {
        // Instantiate the service if Part A was skipped
        if (example1Service == null) example1Service = new Day09Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);

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
