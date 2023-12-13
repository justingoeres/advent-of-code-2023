package org.jgoeres.adventofcode2023.Day12;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class Springs {
    private String pattern;
    private List<Integer> groups;
    private List<List<Integer>> groupValidOffsets = new ArrayList<>();

    public Integer totalMaskBits() {
        // Return the total number of bits masked by all groups (counting the space between groups)
        return getGroups().stream().reduce(0, Integer::sum) + (getGroups().size() - 1);
    }

    public Integer baselineShift(Integer groupNum) {
        // Return the baseline minimum shift required for group 'groupNum'
        // to get it out of the way of the remaining groups
        return getGroups().stream().skip(groupNum + 1).reduce(0, (sum, g) -> sum + (g + 1));
    }

    public Integer totalShift(Integer groupNum, Integer groupShift) {
        return baselineShift(groupNum) + groupShift;
    }

    public void unfold(Integer factor) {
        // Duplicate the pattern and the groups list to a total of 'factor' times larger.
        List<Integer> unfoldedGroups = new ArrayList<>();
        String unfoldedPattern = pattern;
        unfoldedGroups.addAll(groups);
        for (int i = 0; i < (factor - 1); i++) {
            unfoldedPattern += "?" + pattern;
            unfoldedGroups.addAll(groups);
        }
        setPattern(unfoldedPattern);
        setGroups(unfoldedGroups);

        System.out.printf("Unfolded length:\t%d\n", pattern.length());
    }

    public Springs(String pattern, List<Integer> groups) {
        this.pattern = pattern;
        this.groups = groups;
    }
}

enum SpringStatus {
    GOOD('.'),
    BROKEN('#'),
    UNKNOWN('?');

    public final char label;

    private SpringStatus(char label) {
        this.label = label;
    }

    private static final Map<Character, SpringStatus> BY_LABEL = new HashMap<>();

    static {
        for (SpringStatus e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    // ... fields, constructor, methods

    public static SpringStatus fromChar(Character label) {
        return BY_LABEL.get(label);
    }
}