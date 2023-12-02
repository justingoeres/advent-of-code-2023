package org.jgoeres.adventofcode2023.Day02;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@RequiredArgsConstructor
public class MarbleGame {
    private final Integer id;
    private ArrayList<MarbleRound> rounds = new ArrayList<>();

    public void addRound(MarbleRound round) {
        rounds.add(round);
    }

    public boolean allRoundsPossible(final Integer redLimit, final Integer greenLimit, final Integer blueLimit) {
        return rounds.stream().allMatch(r -> r.isPossible(redLimit, greenLimit, blueLimit));
    }

    public Integer minimumPower() {
        /**
         * The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together.
         */

        // Find the minimum set of cubes for this game.
        // i.e. the *max* for each color in all rounds of the game.
        Integer minRed = 0;
        Integer minGreen = 0;
        Integer minBlue = 0;

        for (MarbleRound round : rounds) {
            minRed = (round.getRed() > minRed) ? round.getRed() : minRed;
            minGreen = (round.getGreen() > minGreen) ? round.getGreen() : minGreen;
            minBlue = (round.getBlue() > minBlue) ? round.getBlue() : minBlue;
        }

        final Integer power = minRed * minGreen * minBlue;
        return power;
    }
}
