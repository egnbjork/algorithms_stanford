import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeldKarp {

    private static boolean debug;
    private static double solution;

    public static void main(String[] args) {
        if (args.length == 0) {
            debug = false;
            for (int i = 1; i <= 7; i++) {
                run("test" + i);
            }
        } else {
            debug = true;
            run(args[0]);
        }

        System.out.println("All done!");
    }

    private static void run(String filename) {
        System.out.println("run for " + filename);
        Map<String, Map<Double, Double>> citiesCoords = readFile(filename);
        Map<String, Double> paths = new HashMap<>();

        for (Map.Entry<String, Map<Double, Double>> city : citiesCoords.entrySet()) {
            addCity(paths, city);
            System.out.println(city);
        }
        System.out.println(paths);
    }

    private static void addCity(Map<String, Double> paths,
                                Map.Entry<String, Map<Double, Double>> city) {
        System.out.println(paths);
        paths.put(city.getKey(), 0d);
        if (paths.size() < 2) {
            return;
        }
        System.out.println("Add city " + city.getKey());
        List<String> pathsList = new ArrayList<>(paths.keySet());
        for (String s : pathsList) {
            //to prevent paths from yourself (like AA, BB etc.)
            if (s.equals(city.getKey())) continue;

            String newPath = s + city.getKey();
            List<String> combinations = permute(newPath);
            for (String combination : combinations) {
                if(!paths.containsKey(combination)) {
                    paths.put(combination, 1d);
                }
            }
        }
    }

    public static List<String> permute(String input) {
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
                    solution = Double.parseDouble(line.substring(3));
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
