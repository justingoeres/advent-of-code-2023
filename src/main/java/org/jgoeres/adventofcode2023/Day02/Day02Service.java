package org.jgoeres.adventofcode2023.Day02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02Service {
    public boolean DEBUG = false;

    private ArrayList<MarbleGame> inputList = new ArrayList<>();

    public Day02Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day02Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 2A ===");

        /**
         * Determine which games would have been possible if the bag
         * had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes.
         * What is the sum of the IDs of those games?
         * **/
        final Integer redLimit = 12;
        final Integer greenLimit = 13;
        final Integer blueLimit = 14;

        long result = inputList.stream()
                .filter(game -> game.allRoundsPossible(redLimit, greenLimit, blueLimit))
                .map(game -> game.getId())
                .reduce(0, Integer::sum);
        System.out.println("Day 2A: Sum of IDs of valid games = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 2B ===");

        /**
         * The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together.
         *
         * The power of the minimum set of cubes in game 1 is 48. In games 2-5 it was 12, 1560, 630, and 36,
         * respectively. Adding up these five powers produces the sum 2286.
         *
         * For each game, find the minimum set of cubes that must have been present.
         * What is the sum of the power of these sets?
         * **/

        long result = inputList.stream()
                .map(marbleGame -> marbleGame.minimumPower())
                .reduce(0, Integer::sum);

        System.out.println("Day 2B: Total minimum power = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    /*
        Game 1: 1 red, 3 blue, 11 green; 1 blue, 5 red; 3 blue, 5 green, 13 red; 6 red, 1 blue, 4 green; 16 red, 12 green
        Game 2: 3 red, 13 blue, 5 green; 14 green, 14 blue; 9 blue, 10 green, 3 red; 2 green, 5 blue; 11 green, 3 blue, 3 red; 16 blue, 2 red, 9 green
        Game 3: 17 blue, 5 red; 3 red, 11 green, 17 blue; 1 red, 6 blue, 9 green; 3 blue, 11 green, 1 red; 3 green, 10 red, 11 blue; 12 red, 3 green, 15 blue
    */
    private void loadInputs(String pathToFile) {
        inputList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
            final Pattern p1 = Pattern.compile("^Game (\\d+):\\s(.*)");  // extract the game id
            final Pattern p2 = Pattern.compile("(\\d+)\\s(red|green|blue),?");   // extract each reg/green/blue count
            while ((line = br.readLine()) != null) {
                // process the line.

                final Matcher m1 = p1.matcher(line);
                if (m1.find()) { // If our regex matched this line
                    // Parse it
                    final Integer id = Integer.parseInt(m1.group(1));
                    final String allRounds = m1.group(2);

                    final MarbleGame marbleGame = new MarbleGame(id);
                    final String[] rounds = allRounds.split(";");
                    for (String round : rounds) {
                        // extract the rounds for this game
                        final Matcher m2 = p2.matcher(round);
                        final MarbleRound marbleRound = new MarbleRound();
                        while (m2.find()) {
                            final Integer count = Integer.parseInt(m2.group(1));
                            final String color = m2.group(2);

                            switch (color) {
                                case "red":
                                    marbleRound.setRed(count);
                                    break;
                                case "green":
                                    marbleRound.setGreen(count);
                                    break;
                                case "blue":
                                    marbleRound.setBlue(count);
                                    break;
                            }
                        }
                        marbleGame.addRound(marbleRound);
                    }
                    inputList.add(marbleGame);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
