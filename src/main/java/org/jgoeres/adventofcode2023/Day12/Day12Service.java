package org.jgoeres.adventofcode2023.Day12;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.jgoeres.adventofcode2023.Day12.SpringStatus.BROKEN;
import static org.jgoeres.adventofcode2023.Day12.SpringStatus.GOOD;

public class Day12Service {
    public boolean DEBUG = false;

    private ArrayList<Springs> allSprings = new ArrayList<>();

    public Day12Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day12Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 12A ===");

        long result = 0;
        /**
         * For each row, count all the different arrangements of operational
         * and broken springs that meet the given criteria.
         * What is the sum of those counts?
         **/
        for (Springs springs : allSprings) {
            result += countValidArrangements(springs);
        }

        System.out.println("Day 12A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 12B ===");

        long result = 0;
        /**
         * Unfold your condition records; what is the new sum of possible arrangement counts?
         **/

        // Unfold all the springs x 5
        for (Springs springs : allSprings) {
            springs.unfold(5);
            result += countValidArrangements(springs);
        }

        System.out.println("Day 12B: Answer = " + result);
        return result;
    }

    private Integer countValidArrangements(Springs springs) {
        // Make the groups into bitmasks we can use to check patterns
        final List<Integer> groups = springs.getGroups().stream().map(g -> generateGroupMask(g)).collect(Collectors.toList());
        final Integer numGroups = springs.getGroups().size();  // The number of groups/masks we have to manipulate
        final Integer maxShift = springs.getPattern().length() - springs.totalMaskBits();
        final Integer iMax = 1 << (maxShift);

        Integer count = 0;
        // Compute the masks
        Integer springsBrokenMask = 0;
        Integer springsGoodMask = 0;
        final Integer patternLength = springs.getPattern().length();
        for (int i = 0; i < patternLength; i++) {
            // Broken ones
            final String pattern = springs.getPattern();
            if (pattern.charAt(i) == BROKEN.label) {
                springsBrokenMask |= 1 << ((patternLength - 1) - i);
            }
            if (pattern.charAt(i) == GOOD.label) {
                springsGoodMask |= 1 << ((patternLength - 1) - i);
            }
        }
//        System.out.printf("broken springs mask:\t%s\n", Integer.toBinaryString(springsBrokenMask));
//        System.out.printf("good springs mask:\t%s\n", Integer.toBinaryString(springsGoodMask));


        // iterate through all combinations of the shifted springs
        final List<List<Integer>> allShifts = allShifts(maxShift, numGroups);

        // Now apply all the shifts and test them
        for (List<Integer> shift : allShifts) {
            Integer springsPosition = 0;
            // Build up the overall mask for this shift
//            System.out.printf("Next shifts:\t%s\n", shift.toString());
            for (int i = 0; i < groups.size(); i++) {
                // Figure out the total shift for each group
                Integer shiftedGroup = groups.get(i) << springs.totalShift(i, shift.get(i));
//                System.out.printf("shifted current group:\t%s\n", Integer.toBinaryString(shiftedGroup));
                // And add it to the mask
                springsPosition |= shiftedGroup;
            }
//            System.out.printf("spring arrangement:\t%s\n", Integer.toBinaryString(springsPosition));

            // Now we have the arrangement we need to check. Is it a valid arrangement given what we know?
            // Starting with 'pattern', our mask needs a '1' wherever there is a #
            // And a '0' wherever there is a .

            // Are the broken ones in the right place?
            Boolean brokenSprings = (springsPosition & springsBrokenMask) == springsBrokenMask;
            // Are the good ones in the right place?
            Boolean goodSprings = (~springsPosition & springsGoodMask) == springsGoodMask;
//            System.out.printf("broken OK?\t%s\tgood OK?\t%s\n", brokenSprings, goodSprings);

            if (brokenSprings && goodSprings) {
                count++;
            }
        }
        return count;
    }


    private List<List<Integer>> findAllValidGroupOffsets(Springs springs) {
        final Integer numGroups = springs.getGroups().size();
        Integer minShift = 0;
        Integer maxShift = 0;
        List<List<Integer>> allGroupShifts = new ArrayList<>();
        for (int i = 0; i < numGroups; i++) {
            // Each group must be shifted by at least the minimum valid shift
            // of the group behind it, plus its own length
            for (int j = 0; j < i; j++) {
                List<Integer> prevGroupShifts = allGroupShifts.get(j);
                minShift += prevGroupShifts.get(prevGroupShifts.size()) - 1;
            }
            
        }
        // Starting with the first group, figure out what the _latest_ in the pattern is
        // that it could possibly be.

        // Then track it backward toward the beginning of the pattern to find the valid spots.
        return null;
    }


    private Integer generateGroupMask(final Integer groupLength) {
        Integer mask = 0;
        for (int i = 0; i < groupLength; i++) {
            mask = (mask << 1) | 1;
        }
        return mask;
    }

    private List<List<Integer>> allShifts(final Integer maxShift, Integer numGroups) {
        List<List<Integer>> allShifts = new ArrayList<>();
        List<Integer> shifts = IntStream.rangeClosed(0, maxShift).boxed().collect(Collectors.toList());
        int[] intShifts = new int[numGroups];
        calculateAllShifts(intShifts, 0, allShifts, maxShift);
        return allShifts;
    }

    private void calculateAllShifts(final int[] intShifts, final int position, List<List<Integer>> allShifts, final int limit) {
        if (position == intShifts.length) {
            List<Integer> newShifts = new ArrayList<>();
            for (int i = 0; i < intShifts.length; i++) {
                newShifts.add(intShifts[i]);
            }
            allShifts.add(newShifts);
            return;
        }
        for (int i = 0; i <= limit; i++) {
            intShifts[position] = i;
            calculateAllShifts(intShifts, position + 1, allShifts, i);
        }
    }


    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        allSprings.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] halves = line.split(" ");
                List<Integer> groups = Arrays.stream(halves[1].split(","))
                        .mapToInt(n -> Integer.parseInt(n))
                        .boxed().collect(Collectors.toList());
                allSprings.add(new Springs(halves[0], groups));
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
