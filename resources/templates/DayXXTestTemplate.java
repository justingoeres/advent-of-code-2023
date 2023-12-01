package org.jgoeres.adventofcode${AOC_YEAR};

import org.jgoeres.adventofcode${AOC_YEAR}.Day${AOC_DAY}.Day${AOC_DAY}Service;
import org.jgoeres.adventofcode.common.ToClipboard;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Day${AOC_DAY}Test {
    // Puzzle
    private final String PUZZLE_INPUT = "data/day${AOC_DAY}/input.txt";
    private final boolean PUZZLE_DEBUG = false;
    private Day${AOC_DAY}Service day${AOC_DAY}Service = null;

    // Examples
    private final String EXAMPLE1_INPUT = "data/day${AOC_DAY}/example1.txt";
    private final boolean EXAMPLE_DEBUG = false;
    private Day${AOC_DAY}Service example1Service = null;

    @Test
    @Order(1)   // Run before Puzzle Part B
    public void Day${AOC_DAY}A() {
        if (day${AOC_DAY}Service == null) {
            day${AOC_DAY}Service = new Day${AOC_DAY}Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = day${AOC_DAY}Service.doPartA();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(2)   // Run after Puzzle Part A
    public void Day${AOC_DAY}B() {
        if (day${AOC_DAY}Service == null) {
            day${AOC_DAY}Service = new Day${AOC_DAY}Service(PUZZLE_INPUT, PUZZLE_DEBUG);
        }

        final long EXPECTED = 0;
        long result = 0;
        try {
            result = day${AOC_DAY}Service.doPartB();
            ToClipboard.set(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals(EXPECTED, result);
    }

    @Test
    @Order(3)   // Run before Example Part B
    @Disabled
    public void Day${AOC_DAY}AExample1() {
        example1Service = new Day${AOC_DAY}Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);
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
    public void Day${AOC_DAY}BExample1() {
        // Instantiate the service if Part A was skipped
        if (example1Service == null) example1Service = new Day${AOC_DAY}Service(EXAMPLE1_INPUT, EXAMPLE_DEBUG);

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
