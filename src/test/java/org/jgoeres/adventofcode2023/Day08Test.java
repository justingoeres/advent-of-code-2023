package org.jgoeres.adventofcode2023;

import org.jgoeres.adventofcode2023.Day08.Day08Service;
import org.jgoeres.adventofcode.common.ToClipboard;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Day08Test {
    // Puzzle
    private final String PUZZLE_INPUT = "data/day08/input.txt";
    private final boolean PUZZLE_DEBUG = false;
    private Day08Service day08Service = null;

    // Examples
    private final String EXAMPLE1_INPUT = "data/day08/example1.txt";
    private final boolean EXAMPLE_DEBUG = false;
    private Day08Service example1Service = null;

    @Test
    @Order(1)   // Run before Puzzle Part B
    public void Day08A() {
        if (day08Service == null) {
            day08Service = new Day08Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 19783;
        long result = 0;
        try {
            result = day08Service.doPartA();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(2)   // Run after Puzzle Part A
    public void Day08B() {
        if (day08Service == null) {
            day08Service = new Day08Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 9177460370549L;
        long result = 0;
        try {
            result = day08Service.doPartB();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(3)   // Run before Example Part B
    @Disabled
    public void Day08AExample1() {
        example1Service = new Day08Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);
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
    public void Day08BExample1() {
        // Instantiate the service if Part A was skipped
        if (example1Service == null) example1Service = new Day08Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);

        final long EXPECTED = 6;
        long result = 0;
        try {
            result = example1Service.doPartB();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }
}
