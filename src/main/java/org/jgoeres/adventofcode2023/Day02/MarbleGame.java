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
}
