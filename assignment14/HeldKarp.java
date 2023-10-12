import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.*;

public class HeldKarp {
    private static String solution;

    public static void main(String[] args) {
        if (args.length == 0) {
            for (int i = 1; i <= 7; i++) {
                double actualValue = run("test" + i);
                DecimalFormat df = new DecimalFormat("#.00");
                String formattedValue = df.format(actualValue).replace(",", ".");
                if (solution.equals(formattedValue)) {
                    System.out.println("==================================================");
                    System.out.println("test " + i + " passed!");
                    System.out.println("==================================================\n\n\n\n");
                } else {
                    System.out.println("got " + formattedValue + " instead of " + solution);
                    System.exit(0);
                }

            }
        } else {
            System.out.println(run(args[0]));
        }

        System.out.println("All done!");
    }

    private static double run(String filename) {
        System.out.println("run for " + filename);
        Map<String, Map<Double, Double>> citiesCoords = readFile(filename);
        Map<String, Double> paths = new HashMap<>();

        for (Map.Entry<String, Map<Double, Double>> city : citiesCoords.entrySet()) {
            addCity(paths, citiesCoords, city.getKey());
            System.out.println(city);
        }

        return getShortestPath(citiesCoords, paths);
    }

    private static void addCity(Map<String, Double> paths,
                                Map<String, Map<Double, Double>> citiesCoords,
                                String city) {
        paths.put(city, 0d);
        if (paths.size() < 2) {
            return;
        }
        System.out.println("Add city " + ((int) city.charAt(0) - 65) + " to " + paths.size() + " paths");
        List<String> pathsList = new ArrayList<>(paths.keySet());
        for (String s : pathsList) {
            //to prevent paths from yourself (like AA, BB etc.)
            if (s.endsWith(city)) continue;

            String newPath = s + city;
            List<String> combinations = permute(newPath);
            for (String combination : combinations) {
                if (!paths.containsKey(combination)) {
                    paths.put(combination, computeDistance(combination, citiesCoords, city, paths));
                }
            }
        }
    }

    private static double getShortestPath(Map<String, Map<Double, Double>> citiesCoords,
                                          Map<String, Double> paths) {
        System.out.println("Going back home");
        int maxPathLength = paths.keySet().stream()
                .sorted(Comparator.comparing(String::length))
                .reduce((first, second) -> second)
                .orElseThrow(() -> new IllegalStateException("paths are empty")).length();
        List<String> combinations = paths.keySet()
                .stream()
                .filter(n -> n.length() == maxPathLength)
                .filter(n -> (n.startsWith("A") && (!n.endsWith("A"))))
                .toList();

        Map<String, Double> shortestPaths = new HashMap<>();
        for (String finalCombination : combinations) {
            shortestPaths.put(finalCombination + "A",
                    computeDistance(finalCombination + "A", citiesCoords, "A", paths));
        }

        return shortestPaths.values().stream().sorted().findFirst().orElseThrow();
    }

    private static double computeDistance(String combination,
                                          Map<String, Map<Double, Double>> citiesCoords,
                                          String newCity,
                                          Map<String, Double> paths) {

        if (combination.length() == 2) {
            char firstCity = combination.charAt(0);
            char secondCity = combination.charAt(1);
            return computeDistancePair(citiesCoords.get(Character.toString(firstCity)),
                    citiesCoords.get(Character.toString(secondCity)));
        }

        String firstPath = "";
        String secondPath = "";

        if (combination.startsWith(newCity)) {
            firstPath = combination.substring(0, 2);
            secondPath = combination.substring(1);
        } else if (combination.endsWith(newCity)) {
            firstPath = combination.substring(0, combination.length() - 1);
            secondPath = combination.substring(combination.length() - 2);
        } else {
            return computeNewDistance(combination, citiesCoords, paths);
        }

        return computeNewDistance(firstPath, citiesCoords, paths) +
                computeNewDistance(secondPath, citiesCoords, paths);
    }

    private static double computeNewDistance(String combination,
                                             Map<String, Map<Double, Double>> citiesCoords,
                                             Map<String, Double> paths) {
        if (combination.isEmpty()) return 0d;
        if (combination.length() == 2) {
            return computeDistancePair(combination, citiesCoords);
        }
        if (paths.containsKey(combination)) {
            return paths.get(combination);
        }

        double distance = 0;
        String newCombination = combination;
        while (newCombination.length() > 1) {
            String pathCut = newCombination.substring(0, 2);
            double pathCutDistance = 0;
            if (paths.containsKey(pathCut)) {
                pathCutDistance = paths.get(pathCut);
            } else {
                pathCutDistance = computeDistancePair(citiesCoords.get(pathCut.charAt(0) + ""),
                        citiesCoords.get(pathCut.charAt(1) + ""));
                paths.put(pathCut, pathCutDistance);
            }
            distance += pathCutDistance;
            newCombination = newCombination.substring(1);
        }
        return distance;
    }

    private static double computeDistancePair(String combination,
                                              Map<String, Map<Double, Double>> citiesCoords) {
        if(combination.length() > 2) {
            throw new IllegalStateException("combination length is " + combination.length());
        }
        return computeDistancePair(
                citiesCoords.get(String.valueOf(combination.charAt(0))),
                citiesCoords.get(String.valueOf(combination.charAt(1)))
        );
    }

    private static double computeDistancePair(Map<Double, Double> firstCity, Map<Double, Double> secondCity) {
        double x = firstCity.keySet().iterator().next();
        double y = firstCity.values().iterator().next();
        double z = secondCity.keySet().iterator().next();
        double w = secondCity.values().iterator().next();
        return Math.sqrt(((x - z) * (x - z)) + ((y - w) * (y - w)));
    }

    private static List<String> permute(String input) {
        List<String> permutations = new ArrayList<>();
        generatePermutationsRecursive("", input, permutations);
        return permutations;
    }

    private static void generatePermutationsRecursive(String prefix, String remaining, List<String> permutations) {
        int length = remaining.length();

        if (length == 0) {
            permutations.add(prefix);
        } else {
            for (int i = 0; i < length; i++) {
                String newPrefix = prefix + remaining.charAt(i);
                String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
                generatePermutationsRecursive(newPrefix, newRemaining, permutations);
            }
        }
    }

    private static Map<String, Map<Double, Double>> readFile(String filename) {
        Map<String, Map<Double, Double>> map = new HashMap<>();
        List<String> coords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("//")) {
                    solution = line.substring(3);
                } else {
                    coords.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < coords.size(); i++) {
            Character cityName = (char) (65 + i);
            String[] cityCoords = coords.get(i).split(" ");
            map.put(String.valueOf(cityName), Map.of(Double.valueOf(cityCoords[0]),
                    Double.valueOf(cityCoords[1])));
        }

        return map;
    }
}
