import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;

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

        // Convert to List<Point> in alphabetical order
        List<Point> cities = new ArrayList<>();
        citiesCoords.keySet().stream().sorted().forEach(city -> {
            Map<Double, Double> coords = citiesCoords.get(city);
            double x = coords.keySet().iterator().next();
            double y = coords.values().iterator().next();
            cities.add(new Point(x, y));
        });

        return heldKarpBitmask(cities);
    }

    private static double heldKarpBitmask(List<Point> cities) {
        int n = cities.size();
        double[][] dp = new double[1 << n][n];

        for (double[] row : dp) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }

        dp[1][0] = 0;

        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if ((mask & (1 << last)) == 0) continue;

                for (int next = 0; next < n; next++) {
                    if ((mask & (1 << next)) != 0) continue;

                    int nextMask = mask | (1 << next);
                    double dist = distance(cities.get(last), cities.get(next));
                    dp[nextMask][next] = Math.min(dp[nextMask][next], dp[mask][last] + dist);
                }
            }
        }

        double minCost = Double.POSITIVE_INFINITY;
        int fullMask = (1 << n) - 1;
        for (int j = 1; j < n; j++) {
            double totalCost = dp[fullMask][j] + distance(cities.get(j), cities.get(0));
            minCost = Math.min(minCost, totalCost);
        }

        return minCost;
    }

    private static double distance(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
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

    private static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
