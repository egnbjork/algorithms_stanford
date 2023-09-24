import java.util.*;
import java.util.stream.*;
import java.io.*;
import java.lang.Math.*;

public class HeldKarp {

  private static String firstCity;

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Please provide file name");
      return;
    }
    Map<String, Map<Double, Double>> citiesCoords = readFile(args[0]);

    Map<String, Double> shortestPaths = shortestPaths(citiesCoords);
    
    System.out.println(shortestPaths);
    Integer pathLength = shortestPaths.keySet()
      .stream()
      .sorted(Comparator.comparing(n -> n.length()))
      .reduce((first, second) -> second)
      .orElseThrow(() -> new IllegalStateException("paths are empty")).length();

    String shortestPathRoute = shortestPaths
      .entrySet()
      .stream()
      .filter(n -> n.getKey().length() == pathLength)
      .sorted(Map.Entry.comparingByValue())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
      .entrySet().iterator().next().getKey();

    System.out.println("\n\n ====>>> Shortest path route is " + shortestPathRoute + " ( " + shortestPaths.get(shortestPathRoute) + " ) ");
  }

  private static Map<String, Double> shortestPaths(Map<String, Map<Double, Double>> citiesCoords) {
    Map<String, Double> paths = new HashMap<>();

    for(String city : citiesCoords.keySet()) {
      addCity(paths, citiesCoords, city);
      System.out.println(city);
    }
    addCity(paths, citiesCoords, firstCity);
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

    List<String> combinations = getAllCombinations(newCityName, paths.keySet());

    for(String combination : combinations) {
      if(combination.length() == 2) {
        //System.out.println("calculate new distance for " + combination + " (" + combination.charAt(0) + combination.charAt(1) + ")");
        //System.out.println(citiesCoords.get(String.valueOf(combination.charAt(0))) + " " +
              //citiesCoords.get(String.valueOf(combination.charAt(1))));
        paths.put(combination,
            calculateDistance(citiesCoords.get(String.valueOf(combination.charAt(0))),
              citiesCoords.get(String.valueOf(combination.charAt(1)))));
        //System.out.println(paths.get(combination));
      } else if(combination.startsWith(newCityName)) {
        //System.out.println("combination " + combination + " starts with " + newCityName);
        double val1 = paths.get(newCityName + combination.charAt(1));
        double val2 = paths.get(combination.substring(1));
        //System.out.println("find distance for " + combination + " (" + newCityName + combination.charAt(1)  + " + " + combination.substring(1) + ")");
        //System.out.println(paths);
        paths.put(combination, val1 + val2);
      } else if (combination.endsWith(newCityName)) {
        String firstPathName = combination.substring(0, combination.length() - 1);
        String secondPathName = combination.charAt(combination.length() - 2) + newCityName;
        System.out.println("combination " + combination + " ends with " + newCityName);
        System.out.println("find distance for " + combination + " (" + firstPathName  + " + " + secondPathName + ")");
        double val1 = paths.get(firstPathName);
        double val2 = paths.get(secondPathName);
        paths.put(combination, val1 + val2);
      } else {
        String firstPathName = combination.substring(0, combination.indexOf(newCityName));
        if (firstPathName.length() == 1) {
          firstPathName = firstPathName + newCityName;
        }
        String secondPathName = combination.substring(combination.indexOf(newCityName), combination.length());
        System.out.println("combination " + combination + " has " + newCityName + " in the middle");
        System.out.println("find distance for " + firstPathName + " + " + secondPathName);
        double val1 = paths.get(firstPathName);
        double val2 = paths.get(secondPathName);
        paths.put(combination, val1 + val2);
      }
    }

    return paths;
  }

  private static double calculateDistance(Map<Double, Double> firstCity, Map<Double, Double> secondCity) {
    double x = firstCity.keySet().iterator().next();
    double y = firstCity.values().iterator().next();
    double z = secondCity.keySet().iterator().next();
    double w = secondCity.values().iterator().next();
    return Math.sqrt(((x - z) * (x - z)) + ((y - w) * (y - w)));
  }

  private static List<String> getAllCombinations(String cityName, Set<String> paths) {
    String latestPath = paths
      .stream()
      .sorted(Comparator.comparing(n -> n.length()))
      .reduce((first, second) -> second)
      .orElseThrow(() -> new IllegalStateException("paths are empty"));

      //System.out.println("latest entry is " + latestPath);
      if(latestPath.length() == 1) {
        firstCity = latestPath;
        paths.remove(latestPath);
      }

      List<String> combinations = new ArrayList<>();

      for(int i = 65; i < cityName.charAt(0); i++) {
        combinations.add((char)i + cityName);
        combinations.add(cityName + (char)i);
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

      if(!cityPathsToConsider.isEmpty() &&
          cityPathsToConsider.get(0).indexOf(cityName) >= 0) {
        System.out.println("go back to the home city");
        for(String cityPath : cityPathsToConsider) {
          //System.out.println(cityPath + cityName);
          if(!cityPath.endsWith(cityName)) {
            combinations.add(cityPath + cityName);
          }
        }
      } else {
        for(String cityPath : cityPathsToConsider) {
          //System.out.println("city path " + cityPath);
          combinations.add(cityPath + cityName);
          System.out.println("combination " + new String(cityName + cityPath));
          cityPath = cityName + cityPath;
          char[] cityPathArr = cityPath.toCharArray();
          for(int i = 1; i < cityPath.length(); i++) {
            Character m = cityPathArr[i - 1];
            cityPathArr[i - 1] = cityPathArr[i];
            System.out.println("swap " + m + " with " + cityPathArr[i]);
            cityPathArr[i] = m;
            combinations.add(new String(cityPathArr));
            System.out.println("combination " + new String(cityPathArr));
          }
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
    //System.out.println("Found " +  coords.size() + " cities");

    for (int i = 0; i < coords.size(); i++) {
      Character cityName = (char) (65 + i);
      String[] cityCoords = coords.get(i).split(" ");
      map.put(String.valueOf(cityName), Map.of(Double.valueOf(cityCoords[0]), 
            Double.valueOf(cityCoords[1])));
    }

    return map;
  }
}

