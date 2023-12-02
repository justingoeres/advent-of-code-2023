package org.jgoeres.adventofcode2023.Day02;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MarbleRound {
    private Integer red = 0;
    private Integer green = 0;
    private Integer blue = 0;

    public boolean isPossible(Integer redLimit, Integer greenLimit, Integer blueLimit) {
        return redLimit >= getRed()
                && greenLimit >= getGreen()
                && blueLimit >= getBlue();
    }
}
