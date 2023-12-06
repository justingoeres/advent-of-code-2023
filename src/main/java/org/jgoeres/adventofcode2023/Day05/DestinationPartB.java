package org.jgoeres.adventofcode2023.Day05;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinationPartB {
    private Long destinationValue;
    private Long length;

    public DestinationPartB(Long destinationValue, Long length) {
        this.destinationValue = destinationValue;
        this.length = length;
    }
}
