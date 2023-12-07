package org.jgoeres.adventofcode2023;

import org.jgoeres.adventofcode2023.Day07.Day07Service;
import org.jgoeres.adventofcode.common.ToClipboard;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Day07Test {
    // Puzzle
    private final String PUZZLE_INPUT = "data/day07/input.txt";
    private final boolean PUZZLE_DEBUG = false;
    private Day07Service day07Service = null;

    // Examples
    private final String EXAMPLE1_INPUT = "data/day07/example1.txt";
    private final boolean EXAMPLE_DEBUG = false;
    private Day07Service example1Service = null;

    @Test
    @Order(1)   // Run before Puzzle Part B
    public void Day07A() {
        if (day07Service == null) {
            day07Service = new Day07Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = day07Service.doPartA();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(2)   // Run after Puzzle Part A
    public void Day07B() {
        if (day07Service == null) {
            day07Service = new Day07Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = day07Service.doPartB();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(3)   // Run before Example Part B
    @Disabled
    public void Day07AExample1() {
        example1Service = new Day07Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);
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
    public void Day07BExample1() {
        // Instantiate the service if Part A was skipped
        if (example1Service == null) example1Service = new Day07Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);

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
