import java.util.*;
import java.util.stream.*;
import java.io.*;

public class HeldKarp {
  //add first city to hashtable with coordinates
  //add second city to hashtable with coordinates
  //calculate distance + add to distance table
  //add third city to hashtable with coordinates
  //add third city to every entry in hashtable with distances + swap to get all permutations (ABC ACB CAB)

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Please provide file name");
      return;
    }
    Map<String, Map<Double, Double>> citiesCoords = readFile(args[0]);

    Map<String, Double> shortestPaths = shortestPaths(citiesCoords);
  }

  private static Map<String, Double> shortestPaths(Map<String, Map<Double, Double>> citiesCoords) {
    Map<String, Double> paths = new HashMap<>();

    for(String city : citiesCoords.keySet()) {
      addCity(paths, citiesCoords, city);
      System.out.println(city);
    }
    System.out.println(paths);
    return paths;
  }

  private static Map<String, Double> addCity(Map<String, Double> paths,
      Map<String, Map<Double, Double>> citiesCoords,
      String newCityName) {
    System.out.println("add city " + newCityName + " to the list " + paths);
    if(paths.isEmpty()) {
      paths.put(newCityName, Double.valueOf(0));
      return paths;
    }

    //fill distance table between two cities
    // generate all available variations
    List<String> combinations = getAllCombinations(newCityName, paths.keySet());
    //fill distance for each combination

    for(String combination : combinations) {
      paths.put(combination, Double.valueOf(0));
    }

    return paths;
  }

  private static List<String> getAllCombinations(String cityName, Set<String> paths) {
    String latestPath = paths
      .stream()
      .sorted(Comparator.comparing(n -> n.length()))
      .reduce((first, second) -> second)
      .orElseThrow(() -> new IllegalArgumentException("paths are empty"));

      System.out.println("latest entry is " + latestPath);
      if(latestPath.length() == 1) {
        paths.remove(latestPath);
      }

      List<String> combinations = new ArrayList<>();

      for(int i = 65; i < cityName.charAt(0); i++) {
        combinations.add((char)i + cityName);
      }

      List<String> cityPathsToConsider = paths.stream()
        .filter(n -> n.length() == latestPath.length())
        .collect(Collectors.toList());

      if(cityPathsToConsider.size() == 1) {
          String cityPath = cityPathsToConsider.get(0);
          char[] cityPathArr = cityPath.toCharArray();
          Character m = cityPathArr[0];
          cityPathArr[0] = cityPathArr[1];
          cityPathArr[1] = m;
          cityPathsToConsider.add(new String(cityPathArr));
      }
      System.out.println("city paths to consider: " + cityPathsToConsider);

      for(String cityPath : cityPathsToConsider) {
        System.out.println(cityPath);
        combinations.add(cityName + cityPath);
        cityPath = cityPath + cityName;
        for(int i = 1; i < cityPath.length(); i++) {
          char[] cityPathArr = cityPath.toCharArray();
          Character m = cityPathArr[i - 1];
          cityPathArr[i - 1] = cityPathArr[i];
          cityPathArr[i] = m;
          combinations.add(new String(cityPathArr));
        }
      }

      System.out.println("combinations are: " + combinations);
      return combinations;
  }

  private static Map<String, Map<Double, Double>> readFile(String filename) {
    Map<String, Map<Double, Double>> map = new HashMap<>();
    List<String> coords = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = br.readLine();
      while ((line = br.readLine()) != null) {
        coords.add(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Found " +  coords.size() + " cities");

    for (int i = 0; i < coords.size(); i++) {
      Character cityName = (char) (65 + i);
      String[] cityCoords = coords.get(i).split(" ");
      map.put(String.valueOf(cityName), Map.of(Double.valueOf(cityCoords[0]), 
            Double.valueOf(cityCoords[1])));
    }

    return map;
  }
}

