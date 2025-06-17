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
            String newCity) {
        Set<String> currentCities = paths.keySet().stream()
            .flatMap(s -> Arrays.stream(s.split("")))
            .collect(Collectors.toSet());
        currentCities.add(newCity);

        List<String> newCombinations = generateCityCombinations(currentCities);

        for (String path : newCombinations) {
            if (path.contains(newCity)) {
                paths.put(path, computeDistance(path, citiesCoords, newCity, paths));
            }
        }
    }

    private static List<String> generateCityCombinations(Set<String> cities) {
        List<String> results = new ArrayList<>();
        permute("", String.join("", cities), results);
        return results;
    }

    private static void permute(String prefix, String remaining, List<String> results) {
        if (remaining.isEmpty()) {
            results.add(prefix);
        } else {
            for (int i = 0; i < remaining.length(); i++) {
                permute(prefix + remaining.charAt(i),
                        remaining.substring(0, i) + remaining.substring(i + 1),
                        results);
            }
        }
    }

    private static double computeDistance(String combination,
            Map<String, Map<Double, Double>> citiesCoords,
            String newCity,
            Map<String, Double> paths) {

        if (combination.length() <= 1) return 0;

        double min = Double.POSITIVE_INFINITY;

        for (int i = 1; i < combination.length(); i++) {
            String prev = combination.substring(0, i) + combination.substring(i + 1);
            String last = combination.substring(i, i + 1);
            if (!paths.containsKey(prev)) continue;

            double cost = paths.get(prev) + computeDistancePair(
                    citiesCoords.get(last), citiesCoords.get(combination.substring(0, 1)));
            min = Math.min(min, cost);
        }

        return min;
    }

    private static double getShortestPath(Map<String, Map<Double, Double>> citiesCoords,
            Map<String, Double> paths) {
        int maxLength = citiesCoords.size();

        return paths.entrySet().stream()
            .filter(e -> e.getKey().length() == maxLength)
            .filter(e -> e.getKey().startsWith("A"))
            .mapToDouble(e -> e.getValue() + computeDistancePair(
                        citiesCoords.get(e.getKey().substring(e.getKey().length() - 1)),
                        citiesCoords.get("A")))
            .min().orElse(Double.POSITIVE_INFINITY);
    }

    private static double computeDistancePair(Map<Double, Double> firstCity, Map<Double, Double> secondCity) {
        double x1 = firstCity.keySet().iterator().next();
        double y1 = firstCity.values().iterator().next();
        double x2 = secondCity.keySet().iterator().next();
        double y2 = secondCity.values().iterator().next();
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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

    private static String convertTime(long millis) {
        Duration d = Duration.ofMillis(millis);
        return String.format("%02dh %02dm %02ds %03dms",
                d.toHoursPart(), d.toMinutesPart(), d.toSecondsPart(), d.toMillisPart());
    }
}
