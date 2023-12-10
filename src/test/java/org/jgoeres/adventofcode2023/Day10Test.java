package org.jgoeres.adventofcode2023;

import org.jgoeres.adventofcode2023.Day10.Day10Service;
import org.jgoeres.adventofcode.common.ToClipboard;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Day10Test {
    // Puzzle
    private final String PUZZLE_INPUT = "data/day10/input.txt";
    private final boolean PUZZLE_DEBUG = false;
    private Day10Service day10Service = null;

    // Examples
    private final String EXAMPLE1_INPUT = "data/day10/example1.txt";
    private final String EXAMPLE2_INPUT = "data/day10/example2.txt";
    private final boolean EXAMPLE_DEBUG = false;
    private Day10Service example1Service = null;
    private Day10Service example2Service = null;

    @Test
    @Order(1)   // Run before Puzzle Part B
    public void Day10A() {
        if (day10Service == null) {
            day10Service = new Day10Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 6875;
        long result = 0;
        try {
            result = day10Service.doPartA();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(2)   // Run after Puzzle Part A
    public void Day10B() {
        if (day10Service == null) {
            day10Service = new Day10Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = day10Service.doPartB();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(3)   // Run before Example Part B
    @Disabled
    public void Day10AExample1() {
        example1Service = new Day10Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);
        final long EXPECTED = 4;
        long result = 0;
        try {
            result = example1Service.doPartA();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(3)   // Run before Example Part B
    @Disabled
    public void Day10AExample2() {
        example2Service = new Day10Service(EXAMPLE2_INPUT, EXAMPLE_DEBUG);
        final long EXPECTED = 8;
        long result = 0;
        try {
            result = example2Service.doPartA();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(4)   // Run after Example Part A
    @Disabled
    public void Day10BExample1() {
        // Instantiate the service if Part A was skipped
        if (example1Service == null) example1Service = new Day10Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);

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
