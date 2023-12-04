package org.jgoeres.adventofcode2023.Day04;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class Scratchcard {
    private final Set<Integer> winningNumbers;
    private final Set<Integer> gameNumbers;

    public long calculateScore() {
        long winningCount = winningNumbers.stream()
                .filter(n -> gameNumbers.contains(n))
                .count();

        long score = (winningCount == 0) ? 0 : (long) Math.pow(2, winningCount - 1);
        return score;
    }
}
