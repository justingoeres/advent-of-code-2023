package org.jgoeres.adventofcode2023.Day04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04Service {
    public boolean DEBUG = false;

    private ArrayList<Scratchcard> scratchcards = new ArrayList<>();

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

        long result = scratchcards.stream().map(c -> c.calculateScore()).reduce(0L, Long::sum);
        System.out.println("Day 4A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 4B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 4B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and apply a regex to extract fields
    private void loadInputs(String pathToFile) {
        scratchcards.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            Integer nextInt = 0;
            /** Replace this regex **/
            Pattern p = Pattern.compile("\\d+");
            while ((line = br.readLine()) != null) {
                // process the line.
                String[] sides = line.split(":|\\|");
                Set<Integer> winningNumbers = new HashSet<>();
                Set<Integer> gameNumbers = new HashSet<>();

                Matcher m1 = p.matcher(sides[1]);   // winning numbers
                Matcher m2 = p.matcher(sides[2]);   // game numbers

                while (m1.find()) { // Read the winning numbers
                    Integer winningNum = Integer.parseInt(m1.group(0));
                    winningNumbers.add(winningNum);
                }
                while (m2.find()) { // Read the 'game' numbers
                    Integer gameNumber = Integer.parseInt(m2.group(0));
                    gameNumbers.add(gameNumber);
                }
                scratchcards.add(new Scratchcard(winningNumbers, gameNumbers));
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
