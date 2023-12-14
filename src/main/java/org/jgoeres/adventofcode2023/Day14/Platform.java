package org.jgoeres.adventofcode2023.Day14;

import lombok.Getter;
import lombok.Setter;
import org.jgoeres.adventofcode.common.XYPoint;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
public class Platform {
    public static final char ROCK_CUBE = '#';
    public static final char ROCK_ROUND = 'O';
    public static final char EMPTY = '.';

    private final Comparator<XYPoint> xySortAscending = Comparator.comparing(XYPoint::getY)
            .thenComparing(XYPoint::getX);

    private final Map<String, Character> map = new TreeMap<>();
    private final Map<String, Character> original = new TreeMap<>();
    private Long xMax = 0L;
    private Long yMax = 0L;

    public Character put(XYPoint xy, Character item) {
        if (xy.getX() > xMax) xMax = xy.getX();
        if (xy.getY() > yMax) yMax = xy.getY();
        return map.put(xy.toString(), item);
    }

    public void print() {
        XYPoint xy = new XYPoint();
        for (int y = 0; y <= yMax; y++) {
            for (int x = 0; x <= xMax; x++) {
                xy.set(x, y);
                Character charOut = (map.containsKey(xy.toString())) ? map.get(xy.toString()) : EMPTY;
                System.out.printf("%s", charOut);
            }
            // end of line
            System.out.println();
        }
    }

    public void save() {
        // Store the current map as "original" so we can revert to it.
        original.clear();
        for (Map.Entry<String, Character> entry : map.entrySet()) {
            original.put(entry.getKey(), entry.getValue());
        }
    }

    public void revert() {
        // Replace the current map with a copy of original
        map.clear();
        for (Map.Entry<String, Character> entry : original.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
    }


}

