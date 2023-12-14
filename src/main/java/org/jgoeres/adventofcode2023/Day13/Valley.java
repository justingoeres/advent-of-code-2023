package org.jgoeres.adventofcode2023.Day13;

import lombok.Getter;
import lombok.Setter;
import org.jgoeres.adventofcode.common.XYPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Valley {
    private static final char ROCK = '#';
    private static final char ASH = '.';

    private final Map<XYPoint, Character> map = new HashMap<>();
    private final List<Long> rowValues = new ArrayList<>();
    private final List<Long> colValues = new ArrayList<>();

    private Long xMax = 0L;
    private Long yMax = 0L;


    public Character put(XYPoint xy, Character item) {
        if (xy.getX() > xMax) xMax = xy.getX();
        if (xy.getY() > yMax) yMax = xy.getY();
        return map.put(xy, item);
    }

    public void calculateRowsAndCols() {
        // Calculate the numeric value of the rows & columns
        // Down the rows
        XYPoint xy = new XYPoint();
        for (int y = 0; y <= yMax; y++) {
            Long rowValue = 0L;
            // Across the columns
            for (int x = 0; x <= xMax; x++) {
                xy.set(x, y);
                if (map.get(xy) == ROCK) {
                    rowValue |= 1L << (xMax - x); // flip x so it prints nice
                }
            }
//            System.out.printf("row value:\t%s\n", Long.toBinaryString(rowValue));
            rowValues.add(rowValue);
        }
        // Across the columns
        for (int x = 0; x <= xMax; x++) {
            Long colValue = 0L;
            // Down the rows
            for (int y = 0; y <= yMax; y++) {
                // Across the columns
                xy.set(x, y);
                if (map.get(xy) == ROCK) {
                    colValue |= 1L << (yMax - y); // flip x so it prints nice
                }
            }
//            System.out.printf("col value:\t%s\n", Long.toBinaryString(colValue));
            colValues.add(colValue);
        }
    }
}
