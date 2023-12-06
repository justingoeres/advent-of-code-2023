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

    public Day05Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day05Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 5A ===");

        /**
         * What is the lowest location number that corresponds to any of the
         * initial seed numbers?
         **/

        // Just iterate through all the seeds
        Long minLocation = Long.MAX_VALUE;
        for (Long seed : seeds) {
            final Long finalLocation = getFinalLocation(seed);
            if ((finalLocation) < minLocation) minLocation = finalLocation;
        }
        System.out.println("Day 5A: Minimum seed location = " + minLocation);
        return minLocation;
    }

    public long doPartB() {
        System.out.println("=== DAY 5B ===");

        /**
         * Oops! The seed line actually describes ranges of seed numbers!
         *
         * What is the lowest location number that corresponds to any of
         * the initial seed numbers?
         **/

        // For each seed range, we can start at the bottom of the range (lowest seed, call it s)
        // and map it through the layers. Because of the way the ranges work, wherever s
        // ends up as a final location, s+1's final location will be ONE MORE than that,
        // UNLESS s+1 causes one of the map ranges to cross a breakpoint.
        //
        // That means if we pay attention to the *minimum length* of the maps as we traverse
        // them for s, that tells us that everything between s and s + min_length
        // will be a contiguous set, AND that the final location of s is the minimum location of that set.

        final List<TreeMap<Long, GardenDestination>> allMaps = List.of(seedToSoil,
                soilToFertilizer,
                fertilizerToWater,
                waterToLight,
                lightToTemp,
                tempToHumidity,
                humidityToLocation);

        Long minFinalLocation = Long.MAX_VALUE;
        // For each range...
        for (SeedRange seedRange : seedRanges) {
            // Iterate over the full length of that range
            for (Long seed = seedRange.getStart(); seed <= seedRange.getEnd(); seed++) {
                // Map it through the whole stack
                Long minRange = Long.MAX_VALUE;
                DestinationPartB destination;
                Long mapSeed = seed;
                for (TreeMap<Long, GardenDestination> map : allMaps) {
                    if (DEBUG) System.out.printf("Seed:\t%d\tMap Seed:\t%d\tMinimum Range:\t%d\n", seed, mapSeed, minRange);
                    destination = getMapDestinationPartB(mapSeed, map);
                    // Update the minimum range so far
                    minRange = Math.min(destination.getLength(), minRange);
                    // Update the seed location for the next map level
                    mapSeed = destination.getDestinationValue();
                }
                // Now we should have the final seed location AND the minimum range of the map stack it went through
                // Update the minimum final location
                minFinalLocation = Math.min(mapSeed, minFinalLocation);
                if (DEBUG) System.out.printf("Finished seed:\t%d\tFinal location:\t%d (minimum %d)\tJump ahead by:\t%d\n", seed, mapSeed, minFinalLocation, minRange);

                // Finally, jump the 'seed' ahead by the minimum contiguous range in the map stack
                seed += minRange;
            }
        }

        System.out.println("Day 5B: Minimum final seed location = " + minFinalLocation);
        return minFinalLocation;
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

    public DestinationPartB getMapDestinationPartB(Long source, TreeMap<Long, GardenDestination> map) {
        Map.Entry<Long, GardenDestination> destination = findOutputGardenDestination(source, map);

        Long destinationValue;
        Long destinationEnd;

        if (destination != null) {
            destinationValue = destination.getValue().getStart() + (source - destination.getKey());
            destinationEnd = destination.getValue().getEnd();
        } else {
            destinationValue = source;
            destinationEnd = Long.MAX_VALUE;
        }
        // We have the value that source maps, too; now find how much of this 'destination range' is left
        Long destinationRange = destinationEnd - destinationValue;

        return new DestinationPartB(destinationValue, destinationRange);
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
