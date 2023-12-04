package org.jgoeres.adventofcode2023.Day04;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class Scratchcard {
    private final Integer id;
    private final Set<Integer> winningNumbers;
    private final Set<Integer> gameNumbers;

    private long totalCount = 0;

    public long calculateScore() {
        final long winningCount = countWinners();

        return (winningCount == 0) ? 0 : (long) Math.pow(2, winningCount - 1);
    }

    private long countWinners() {
        return winningNumbers.stream()
                .filter(n -> gameNumbers.contains(n))
                .count();
    }

    public long countAllWinners(final Map<Integer, Scratchcard> allCards) {
        // First, if we're already calculated for this card before, return that result
        if (totalCount > 0) return totalCount;

        // for the current card find out how many *more* cards we should process
        long totalCount = 1; // ourselves
        long addlCards = countWinners();    // how many under this card to process

        if (countWinners() == 0) return updateAndReturnTotalCount(totalCount); // no more, just this card

        // Get each winning card and process it
        for (int i = 1; i <= addlCards; i++) {
            totalCount += allCards.get(id + i).countAllWinners(allCards);
        }
        return updateAndReturnTotalCount(totalCount);
    }

    private long updateAndReturnTotalCount(final long totalCount) {
        // cache the totalCount for this card so we don't have to recalculate
        this.totalCount = totalCount;
        return this.totalCount;
    }
}
