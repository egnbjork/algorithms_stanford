import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class TSPHeuristic {

  private static boolean debug = true;

  public static void main(String[] args) {
    if (args.length == 0) {
      debug = false;
      for(int i = 1; i <=7; i++) {
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
    Map<Integer, Map<Double, Double>> citiesCoords = readFile(filename);
    System.out.println(citiesCoords);
  }

  private static double calculateDistance(Map<Double, Double> firstCity, Map<Double, Double> secondCity) {
    double x = firstCity.keySet().iterator().next();
    double y = firstCity.values().iterator().next();
    double z = secondCity.keySet().iterator().next();
    double w = secondCity.values().iterator().next();
    return Math.sqrt(((x - z) * (x - z)) + ((y - w) * (y - w)));
  }

  private static Map<Integer, Map<Double, Double>> readFile(String filename) {
    Map<Integer, Map<Double, Double>> map = new HashMap<>();
    List<String> coords = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = br.readLine();
      while ((line = br.readLine()) != null) {
        if(!line.startsWith("//")) {
          coords.add(line);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (int i = 0; i < coords.size(); i++) {
      String[] cityCoords = coords.get(i).split(" ");
      Integer cityName = Integer.valueOf(cityCoords[0]);
      map.put(cityName, Map.of(Double.valueOf(cityCoords[1]),
                                               Double.valueOf(cityCoords[2])));
    }

    return map;
  }
}
