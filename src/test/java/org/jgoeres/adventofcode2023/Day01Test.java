package org.jgoeres.adventofcode2023;

import org.jgoeres.adventofcode2023.Day01.Day01Service;
import org.jgoeres.adventofcode.common.ToClipboard;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Day01Test {
    // Puzzle
    private final String PUZZLE_INPUT = "data/day01/input.txt";
    private final boolean PUZZLE_DEBUG = false;
    private Day01Service day01Service = null;

    // Examples
    private final String EXAMPLE1_INPUT = "data/day01/example1.txt";
    private final String EXAMPLE2_INPUT = "data/day01/example2.txt";
    private final boolean EXAMPLE_DEBUG = false;
    private Day01Service example1Service = null;

    @Test
    @Order(1)   // Run before Puzzle Part B
    public void Day01A() {
        if (day01Service == null) {
            day01Service = new Day01Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }
        final long EXPECTED = 54916;
        long result = 0;
        try {
            result = day01Service.doPartA();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(2)   // Run after Puzzle Part A
    public void Day01B() {
        if (day01Service == null) {
            day01Service = new Day01Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }
        final long EXPECTED = 54728;
        long result = 0;
        try {
            result = day01Service.doPartB();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(3)   // Run before Example Part B
    @Disabled
    public void Day01AExample1() {
        example1Service = new Day01Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);
        final long EXPECTED = 142;
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
    public void Day01BExample1() {
        // Instantiate the service if Part A was skipped
        if (example1Service == null) example1Service = new Day01Service(EXAMPLE2_INPUT, EXAMPLE_DEBUG);

        final long EXPECTED = 281;
        long result = 0;
        try {
            result = example1Service.doPartB();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }
}
