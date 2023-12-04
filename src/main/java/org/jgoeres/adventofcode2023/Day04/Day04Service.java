package org.jgoeres.adventofcode2023.Day04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04Service {
    public boolean DEBUG = false;

    private Map<Integer, Scratchcard> scratchcards = new HashMap();

    public Day04Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day04Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 4A ===");

        /**
         * The first match makes the card worth one point and each match after the first doubles the point value of that card
         *
         * Calculate the scores of all the cards and add them up.
         **/

        long result = scratchcards.values().stream().map(c -> c.calculateScore()).reduce(0L, Long::sum);
        System.out.println("Day 4A: Sum of all scratchcard scores = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 4B ===");

        /**
         * You win copies of the scratchcards below the winning card equal to the number of matches.
         *
         * Process all of the original and copied scratchcards until no more scratchcards are won.
         * Including the original set of scratchcards, how many total scratchcards do you end up with?
         **/

        // Note we're not scoring, we're just counting number of *cards*
        long result = scratchcards.values().stream()
                .map(c -> c.countAllWinners(scratchcards))
                .reduce(0L, Long::sum);

        System.out.println("Day 4B: Total number of scratchcards = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        scratchcards.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            Pattern p = Pattern.compile("\\d+");
            while ((line = br.readLine()) != null) {
                // process the line.
                String[] gameStrings = line.split(":|\\|");
                Set<Integer> winningNumbers = new HashSet<>();
                Set<Integer> gameNumbers = new HashSet<>();

                Matcher m0 = p.matcher(gameStrings[0]);   // game id
                Matcher m1 = p.matcher(gameStrings[1]);   // winning numbers
                Matcher m2 = p.matcher(gameStrings[2]);   // game numbers

                m0.find();
                Integer gameId = Integer.parseInt(m0.group(0)); // Store the game id

                while (m1.find()) { // Read the winning numbers
                    Integer winningNum = Integer.parseInt(m1.group(0));
                    winningNumbers.add(winningNum);
                }
                while (m2.find()) { // Read the 'game' numbers
                    Integer gameNumber = Integer.parseInt(m2.group(0));
                    gameNumbers.add(gameNumber);
                }
                scratchcards.put(gameId, new Scratchcard(gameId, winningNumbers, gameNumbers));
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
