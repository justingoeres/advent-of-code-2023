package org.jgoeres.adventofcode2023.Day07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jgoeres.adventofcode2023.Day07.Day07Service.HandType.*;

public class Day07Service {
    public boolean DEBUG = false;

    private TreeMap<String, Long> handsPart1 = new TreeMap<>(new HandStrengthOrderPart1());
    private TreeMap<String, Long> handsPart2 = new TreeMap<>(new HandStrengthOrderPart2());

    enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }

    private static final String JOKER = "J";

    private static final Map<Character, Integer> cardOrder = Map.ofEntries(
            Map.entry('A', 14),
            Map.entry('K', 13),
            Map.entry('Q', 12),
            Map.entry('J', 11),
            Map.entry('T', 10),
            Map.entry('9', 9),
            Map.entry('8', 8),
            Map.entry('7', 7),
            Map.entry('6', 6),
            Map.entry('5', 5),
            Map.entry('4', 4),
            Map.entry('3', 3),
            Map.entry('2', 2)
    );


    public Day07Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day07Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 7A ===");

        /**
         * Find the rank of every hand in your set. What are the total winnings?
         **/
        Long result = 0L;
        Integer rank = 1;
        for (Map.Entry<String, Long> hand : handsPart1.entrySet()) {
            result += rank * hand.getValue();
            if (DEBUG)
                System.out.printf("%s\t%s\trank:\t%d\tscore:\t%d\ttotal result:\t%d\n", hand.getKey(), classifyHandPart1(hand.getKey()), rank, hand.getValue(), result);
            rank++;
        }

        System.out.println("Day 7A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 7B ===");

        /** Put problem implementation here **/
        Long result = 0L;
        Integer rank = 1;
        for (Map.Entry<String, Long> hand : handsPart2.entrySet()) {
            result += rank * hand.getValue();
            if (DEBUG)
                System.out.printf("%s\t%s\trank:\t%d\tscore:\t%d\ttotal result:\t%d\n", hand.getKey(), classifyHandPart2(hand.getKey()), rank, hand.getValue(), result);
            rank++;
        }

        System.out.println("Day 7B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        handsPart1.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
            Pattern p = Pattern.compile("([AKQJT0-9]{5}) (\\d+)");
            while ((line = br.readLine()) != null) {
                // process the line.
                Matcher m = p.matcher(line);
                if (m.find()) { // If our regex matched this line
                    // Parse it
                    String cards = m.group(1);
                    Long bid = Long.parseLong(m.group(2));
                    handsPart1.put(cards, bid);
                    handsPart2.put(cards, bid);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private static final List<Pattern> CLASSIFIERS = List.of(
            Pattern.compile("(.)\\1{4}"),   // FIVE_OF_A_KIND
            Pattern.compile("(.)\\1{3}"),   // FOUR_OF_A_KIND
            Pattern.compile("(.)\\1{2}(.)\\2"),   // FULL_HOUSE, e.g. AAAQQ
            Pattern.compile("(.)\\1(.)\\2{2}"),   // FULL_HOUSE the other way, e.g. AAQQQ
            Pattern.compile("(.)\\1{2}"),   // THREE_OF_A_KIND
            Pattern.compile("(.)\\1.?(.)\\2"),   // TWO_PAIR
            Pattern.compile("(.)\\1")   // ONE_PAIR
    );
    
    private static final List<HandType> CLASSES = List.of(
            FIVE_OF_A_KIND,
            FOUR_OF_A_KIND,
            FULL_HOUSE, // one way
            FULL_HOUSE, // the other way
            THREE_OF_A_KIND,
            TWO_PAIR,
            ONE_PAIR
    );

    private static HandType classifyHandPart1(String hand) {
        // First sort the cards so like values are grouped
        String sorted = hand.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        // Now apply a bunch of regexes to identify things.
        for (int i = 0; i < CLASSIFIERS.size(); i++) {
            Pattern classifier = CLASSIFIERS.get(i);
            Matcher m = classifier.matcher(sorted);
            if (m.find()) {
                // if this classifier matches the sorted hand, return the classification
                return CLASSES.get(i);
            }
        }
        // If nothing matched, it's nothing special
        return HIGH_CARD;
    }

    private static HandType classifyHandPart2(String hand) {
        // First classify the hand as in part 1
        HandType handType = classifyHandPart1(hand);

        // Then 'upgrade' it based on the presence of any jokers
        switch (handType) {
            case HIGH_CARD:
                if (hand.contains(JOKER)) handType = ONE_PAIR;
                break;
            case ONE_PAIR:
                if (hand.contains(JOKER)) handType = THREE_OF_A_KIND;
                break;
            case TWO_PAIR:
                long jokerCount = hand.chars().filter(ch -> ch == JOKER.charAt(0)).count();
                // if TWO_PAIR has one joker, it becomes a full house
                if (jokerCount == 1) handType = FULL_HOUSE;
                // if it has two jokers, it becomes 4 of a kind
                if (jokerCount == 2) handType = FOUR_OF_A_KIND;
                break;
            case THREE_OF_A_KIND:
                if (hand.contains(JOKER)) handType = FOUR_OF_A_KIND;
                break;
            case FULL_HOUSE:
            case FOUR_OF_A_KIND:
                // Full house & 4 of a kind both become 5 of a kind
                if (hand.contains(JOKER)) handType = FIVE_OF_A_KIND;
                break;
        }
        return handType;
    }

    static class HandStrengthOrderPart1 implements Comparator<String> {
        @Override
        public int compare(String hand1, String hand2) {
            // return 1 if hand1 is stronger than hand2
            final int comparison = (classifyHandPart1(hand1).compareTo(classifyHandPart1(hand2)));
            if (comparison == 0) {
                // break ties by comparing cards in order
                for (int i = 0; i < hand1.length(); i++) {
                    final Character char1 = hand1.charAt(i);
                    final Character char2 = hand2.charAt(i);
                    int compareResult = compareCards(char1, char2);
                    if (compareResult != 0) return compareResult;
                }
            }
            return comparison;
        }

        private static int compareCards(Character c1, Character c2) {
            return cardOrder.get(c1).compareTo(cardOrder.get(c2));
        }
    }

    static class HandStrengthOrderPart2 implements Comparator<String> {
        @Override
        public int compare(String hand1, String hand2) {
            // return 1 if hand1 is stronger than hand2
            final int comparison = (classifyHandPart2(hand1).compareTo(classifyHandPart2(hand2)));
            if (comparison == 0) {
                // break ties by comparing cards in order
                for (int i = 0; i < hand1.length(); i++) {
                    final Character char1 = hand1.charAt(i);
                    final Character char2 = hand2.charAt(i);
                    int compareResult = compareCards(char1, char2);
                    if (compareResult != 0) return compareResult;
                }
            }
            return comparison;
        }

        private static int compareCards(Character c1, Character c2) {
            return cardOrder.get(c1).compareTo(cardOrder.get(c2));
        }
    }
}
