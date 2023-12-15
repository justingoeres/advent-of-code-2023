package org.jgoeres.adventofcode2023.Day15;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jgoeres.adventofcode2023.Day15.Day15Service.calculateHash;

@Getter
@Setter
@RequiredArgsConstructor
public class Step {
    private final String label;
    private final Long hash;
    private final Character operation;
    private final Long focalLength;

    private static final Pattern p = Pattern.compile("([a-z]+)(=|-)(\\d+)?");
    public static final Character REMOVE = '-';
    public static final Character INSERT = '=';



    public Step(String init) {
        Matcher m = p.matcher(init);
        m.find();
        this.label = m.group(1);
        this.operation = m.group(2).charAt(0);
        if (m.group(2).equals(REMOVE)) {
            this.focalLength = 0L;
        } else {
            this.focalLength = Long.parseLong(m.group(3));
        }
        this.hash = calculateHash(this.label);
    }
}
