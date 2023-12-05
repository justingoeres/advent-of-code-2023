package org.jgoeres.adventofcode2023.Day05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05Service {
    public boolean DEBUG = false;

    private final Set<Long> seeds = new HashSet<>();
    private final Set<SeedRange> seedRanges = new HashSet<>();
    private final TreeMap<Long, GardenDestination> seedToSoil = new TreeMap<>();
    private final TreeMap<Long, GardenDestination> soilToFertilizer = new TreeMap<>();
    private final TreeMap<Long, GardenDestination> fertilizerToWater = new TreeMap<>();
    private final TreeMap<Long, GardenDestination> waterToLight = new TreeMap<>();
    private final TreeMap<Long, GardenDestination> lightToTemp = new TreeMap<>();
    private final TreeMap<Long, GardenDestination> tempToHumidity = new TreeMap<>();
    private final TreeMap<Long, GardenDestination> humidityToLocation = new TreeMap<>();

    private static final GardenDestination STRAIGHT_THROUGH = new GardenDestination(0L, 0L);

//    private ArrayList<IGetMapDestination> allGetMapDestinations = List.of(
//            (source) -> getMapDestination(source,seedToSoil)
//    )

    public Day05Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day05Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 5A ===");

//        System.out.println("seed\tsoil");
//        for (Long i = 0L; i < 100; i++) System.out.println(i + "\t" + getMapDestination(i, seedToSoil));

        /**
         * What is the lowest location number that corresponds to any of the
         * initial seed numbers?
         **/

        // Just iterate through all the seeds
        Long minLocation = Long.MAX_VALUE;
        for (Long seed : seeds) {
//            System.out.println("seed:\t" + seed);
            final Long finalLocation = getFinalLocation(seed);
//            System.out.println("final location:\t" + finalLocation + "\n");
            if ((finalLocation) < minLocation) minLocation = finalLocation;
        }
        System.out.println("Day 5A: Lowest location number = " + minLocation);
        return minLocation;
    }

    public long doPartB() {
        System.out.println("=== DAY 5B ===");

        long result = 0;
        /**
         * Oops! The seed line actually describes ranges of seed numbers!
         *
         * What is the lowest location number that corresponds to any of
         * the initial seed numbers?
         **/

        final Set<SeedRange> allOutputRanges = new HashSet<>();
        for (SeedRange seedRange : seedRanges) {
            allOutputRanges.addAll(calculateOutputRanges(seedRange,seedToSoil));
        }

        for (SeedRange seedRange : seedRanges) {
            allOutputRanges.addAll(

                    calculateOutputRanges(calculateOutputRanges(seedRange,seedToSoil),soilToFertilizer);
        }


        System.out.println("Day 5B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        seeds.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            final Pattern p1 = Pattern.compile("\\d+");
            final Pattern p2 = Pattern.compile("(.*) map:");
            final Pattern p3 = Pattern.compile("(\\d+) (\\d+) (\\d+)");
            final Pattern p4 = Pattern.compile("(\\d+) (\\d+)");
            String currentLabel = "";
            while ((line = br.readLine()) != null) {
                Matcher m2 = p2.matcher(line);  // to find the labels
                Matcher m3 = p3.matcher(line);  // to find the map ranges
                Matcher m4 = p4.matcher(line);  // to find the seed ranges for Part B
                if (seeds.isEmpty()) {
                    Matcher m1 = p1.matcher(line);  // to find the numbers
                    // If we have no seeds, then we're on line 1 so read them
                    while (m1.find()) {
                        // Make the Set of seeds for Part A
                        seeds.add(Long.parseLong(m1.group(0)));
                    }
                    while (m4.find()) {
                        // Make the Set of seed ranges for Part B
                        final Long start = Long.parseLong(m4.group(1));
                        final Long length = Long.parseLong(m4.group(2));
                        seedRanges.add(new SeedRange(start, start + length - 1));
                    }
                } else if (m2.find()) {
                    // If this is a label, update our current label
                    currentLabel = m2.group(1);
                }
                // Otherwise, it's a mapping line (or a blank, which we'll just skip)
                else if (m3.find()) {
                    // Parse the values
                    final Long target = Long.parseLong(m3.group(1));
                    final Long source = Long.parseLong(m3.group(2));
                    final Long length = Long.parseLong(m3.group(3));

                    // Put them in the right place
                    switch (currentLabel) {
                        case "seed-to-soil":
                            seedToSoil.put(source, new GardenDestination(target, length));
                            break;
                        case "soil-to-fertilizer":
                            soilToFertilizer.put(source, new GardenDestination(target, length));
                            break;
                        case "fertilizer-to-water":
                            fertilizerToWater.put(source, new GardenDestination(target, length));
                            break;
                        case "water-to-light":
                            waterToLight.put(source, new GardenDestination(target, length));
                            break;
                        case "light-to-temperature":
                            lightToTemp.put(source, new GardenDestination(target, length));
                            break;
                        case "temperature-to-humidity":
                            tempToHumidity.put(source, new GardenDestination(target, length));
                            break;
                        case "humidity-to-location":
                            humidityToLocation.put(source, new GardenDestination(target, length));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    public Long getMapDestination(Long source, TreeMap<Long, GardenDestination> map) {
        final Map.Entry<Long, GardenDestination> destination = findOutputGardenDestination(source, map);

        Long destinationValue = source;
        if (destination != null) {
            destinationValue = destination.getValue().getStart() + (source - destination.getKey());
        }
        return destinationValue;

    }

    public Map.Entry<Long, GardenDestination> findOutputGardenDestination(Long source, TreeMap<Long, GardenDestination> map) {
        // Figure out which segment of the given map 'source' lands in, and return that.
        // The elements of the TreeMap will come out in order
        // so we can iterate to find where our source falls
        for (Map.Entry<Long, GardenDestination> mapEntry : map.entrySet()) {
            GardenDestination destination = mapEntry.getValue();
            // If our source is greater than the destination start,
            // figure out if it's *in* the destination range
            if (source >= mapEntry.getKey()
                    && source < (mapEntry.getKey() + destination.getLength())) {
                // It's in the range
                return mapEntry;
            }
        }
        // If source doesn't fall into any defined segment, return the identity (straight-through) mapping
        return null;
    }

    public Set<SeedRange> calculateOutputRanges(SeedRange seedRange, TreeMap<Long, GardenDestination> map) {
        // We need to take the input range (start & end) and figure out how it splits
        // into multiple output ranges on its way through the map

        final Set<SeedRange> outputRanges = new HashSet<>();
        // Start at... the start
        for (Long seed = seedRange.getStart(); seed <= seedRange.getEnd(); seed++) {
            // Figure out what output range the 'current' position lands in
            final Map.Entry<Long, GardenDestination> outputMapEntry = findOutputGardenDestination(seed, map);

            // Convert the start of the seed range to the start of the output range
            final Long outputRangeStart = getMapDestination(seed, map);

            // How much of the output range is left?
            final Long outputRangeLeft = outputMapEntry.getValue().getEnd() - outputRangeStart;

            // Does this seed range fit entirely within the output range?
            if (outputRangeLeft >= seedRange.getLength()) {
                final Long outputRangeEnd = outputRangeStart + seedRange.getLength();
                final SeedRange outputRange = new SeedRange(outputRangeStart, outputRangeEnd);
                outputRanges.add(outputRange);
                return outputRanges; // we're done, we've hit the end of the seed range
            } else {
                // This seed range does NOT fit entire in the output range, so we have to split it
                final Long outputRangeEnd = outputRangeStart + outputRangeLeft;
                final SeedRange outputRange = new SeedRange(outputRangeStart, outputRangeEnd);
                outputRanges.add(outputRange);
                // update the seed to the end of the range we just processed
                seed += outputRange.getLength();
            }
        }
        // This should never happen (maybe the For could be a While)
        return null;
    }

    private Long getFinalLocation(Long seed) {
        // The ugly way
        return getMapDestination(
                getMapDestination(
                        getMapDestination(
                                getMapDestination(
                                        getMapDestination(
                                                getMapDestination(
                                                        getMapDestination(seed, seedToSoil),
                                                        soilToFertilizer),
                                                fertilizerToWater),
                                        waterToLight),
                                lightToTemp),
                        tempToHumidity),
                humidityToLocation);
    }
}
