package org.jgoeres.adventofcode2023.Day05;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GardenDestination {
    private final Long start;
    private final Long length;

    public Long getEnd() {
        return start + length - 1;
    }
}