import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class HeldKarp {
    private static String solution;
    private static long start = System.currentTimeMillis();

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
            System.out.println(city + " added");
            System.out.println("took " + convertTime(System.currentTimeMillis() - start));
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
        System.out.println("Add city " + ((int) city.charAt(0) - 64) + " to " + paths.size() + " paths");
        List<String> pathsList = new ArrayList<>(paths.keySet());
        for (String s : pathsList) {
            //to prevent paths from yourself (like AA, BB etc.)
            if (s.endsWith(city)) continue;

            String newPath = s + city;
            List<String> combinations = permute(newPath, paths);
//            System.out.println(combinations.size() + " permitations for string " + city + " and path " + newPath);
//            int count = 0;
            for (String combination : combinations) {
                if (!paths.containsKey(combination)) {
//                    count++;
                    paths.put(combination, computeDistance(combination, citiesCoords, city, paths));
                }
//                System.out.println(count + " keys were calculated");
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
        if (paths.containsKey(combination)) {
            return paths.get(combination);
        }
        if (combination.length() == 2) {
            return computeDistancePair(combination, citiesCoords);
        }

        double distance = 0;
//        String newCombination = combination;
        int pathCutEnd = combination.length() - 2;
        while (!paths.containsKey(combination.substring(0, pathCutEnd))) {
            pathCutEnd--;
        }
        String firstPath = combination.substring(0, pathCutEnd);
        String secondPath = combination.substring(pathCutEnd - 1, pathCutEnd + 1);
        String thirdPath = combination.substring(pathCutEnd, pathCutEnd + 2);
        String fourthPath = combination.substring(pathCutEnd + 1);

        distance += Optional.ofNullable(paths.get(firstPath)).orElse(0D);
        distance += computeDistancePair(secondPath, citiesCoords);
        paths.put(
                combination.substring(0, firstPath.length() + secondPath.length() - 1),
                distance
        );

        distance += computeDistancePair(thirdPath, citiesCoords);
        paths.put(
                combination.substring(0, firstPath.length() + secondPath.length()),
                distance
        );

        distance += Optional.ofNullable(paths.get(fourthPath)).orElse(0D);
        paths.put(combination, distance);
        return distance;
    }

    private static double computeDistancePair(String combination,
                                              Map<String, Map<Double, Double>> citiesCoords) {
        if (combination.length() > 2) {
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

    private static List<String> permute(String input, Map<String, Double> paths) {
        List<String> permutations = new LinkedList<>();

        generatePermutationsRecursive("", input, permutations, paths);
        return permutations;
    }

    private static void generatePermutationsRecursive(String prefix,
                                                      String remaining,
                                                      List<String> permutations,
                                                      Map<String, Double> paths) {
        int length = remaining.length();

        if (length == 0) {
            if(!paths.containsKey(prefix)) {
                permutations.add(prefix);
            }
        } else {
            for (int i = 0; i < length; i++) {
                String newPrefix = prefix + remaining.charAt(i);
                String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
                generatePermutationsRecursive(newPrefix, newRemaining, permutations, paths);
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

    private static String convertTime(long score) {
        StringBuilder sb = new StringBuilder();

        Duration d = Duration.ofMillis(score);
        long hours = d.toHoursPart();
        if(hours > 0) {
            sb.append(hours).append("h ");
        }

        long minutes = d.toMinutesPart();
        if(minutes > 0) {
            sb.append(minutes).append("m ");
        }

        long seconds = d.toSecondsPart();
        if(seconds > 0) {
            sb.append(seconds).append("s ");
        }
        long milliseconds = d.toMillisPart();
        if(milliseconds > 0) {
            sb.append(milliseconds).append("ms ");
        }

        return sb.toString();
    }
}
