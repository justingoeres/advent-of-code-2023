package org.jgoeres.adventofcode2023.Day05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05Service {
    public boolean DEBUG = false;

    private static final Set<Long> seeds = new HashSet<>();
    private static final TreeMap<Long, GardenDestination> seedToSoil = new TreeMap<>();
    private static final TreeMap<Long, GardenDestination> soilToFertilizer = new TreeMap<>();
    private static final TreeMap<Long, GardenDestination> fertilizerToWater = new TreeMap<>();
    private static final TreeMap<Long, GardenDestination> waterToLight = new TreeMap<>();
    private static final TreeMap<Long, GardenDestination> lightToTemp = new TreeMap<>();
    private static final TreeMap<Long, GardenDestination> tempToHumidity = new TreeMap<>();
    private static final TreeMap<Long, GardenDestination> humidityToLocation = new TreeMap<>();

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
        /** Put problem implementation here **/

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
            String currentLabel = "";
            while ((line = br.readLine()) != null) {
                Matcher m2 = p2.matcher(line);  // to find the labels
                Matcher m3 = p3.matcher(line);  // to find the labels
                if (seeds.isEmpty()) {
                    Matcher m1 = p1.matcher(line);  // to find the numbers
                    // If we have no seeds, then we're on line 1 so read them
                    while (m1.find()) seeds.add(Long.parseLong(m1.group(0)));
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
        // The elements of the TreeMap will come out in order
        // so we can iterate to find where our source falls
        for (Map.Entry<Long, GardenDestination> mapEntry : map.entrySet()) {
            GardenDestination destination = mapEntry.getValue();
            // If our source is greater than the destination start,
            // figure out if it's *in* the destination range
            if (source >= mapEntry.getKey()
                    && source < (mapEntry.getKey() + destination.getLength())) {
                // It's in the range, so return the mapped value
                Long destinationValue = destination.getStart() + (source - mapEntry.getKey());
//                System.out.println("destination:\t" + destinationValue);
                return destinationValue;
            } else if (source < mapEntry.getKey()) {
                // If source < the start of this destination, then we're past any entry
                // it could match, so just map it straight through
//                System.out.println("destination:\t" + source + "\t(straight through)");
                return source;
            }
        }
        // If nothing matched at all, map it through
//        System.out.println("destination:\t" + source + "\t(straight through / final case)");
        return source;
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
