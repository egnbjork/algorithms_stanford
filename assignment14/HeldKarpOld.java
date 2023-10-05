import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class HeldKarpOld {

  private static Double solution;
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
    Map<String, Map<Double, Double>> citiesCoords = readFile(filename);

    Map<String, Double> shortestPaths = shortestPaths(citiesCoords);

//     if(debug) System.out.println(shortestPaths);

    Integer pathLength = shortestPaths.keySet()
      .stream()
      .sorted(Comparator.comparing(n -> n.length()))
      .reduce((first, second) -> second)
      .orElseThrow(() -> new IllegalStateException("paths are empty")).length();

    Map.Entry<String, Double> shortestPathRoute = shortestPaths
      .entrySet()
      .stream()
      .filter(n -> n.getKey().startsWith("A"))
      .filter(n -> n.getKey().length() == pathLength)
      .sorted(Map.Entry.comparingByValue())
      .reduce((first, second) -> first)
      .orElseThrow(() -> new IllegalStateException("Something is wrong"));
    //.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    //.entrySet().iterator().next().getKey();

    if (debug) System.out.println("\n\n ====>>> Shortest path route is " + shortestPathRoute);

    if(solution != null) {
      DecimalFormat df = new DecimalFormat("#.##");
      String expectedResult = String.valueOf(solution);
      String actualResult = df.format(shortestPathRoute.getValue());

      if(!actualResult.equals(expectedResult)) {
        System.out.println("got " + actualResult + " while expecting " + expectedResult + " in test case " + filename);
        System.exit(0);
      }
    }
  }

  private static Map<String, Double> shortestPaths(Map<String, Map<Double, Double>> citiesCoords) {
    Map<String, Double> paths = new HashMap<>();

    for(String city : citiesCoords.keySet()) {
      addCity(paths, citiesCoords, city, false);
    }
    return addCity(paths, citiesCoords, "A", true);
  }

  private static Map<String, Double> addCity(Map<String, Double> paths,
                                             Map<String, Map<Double, Double>> citiesCoords,
                                             String newCityName,
                                             Boolean homeCity) {
    if (debug) System.out.println(
        "add city " + newCityName  + ", " + 
        (((int)(newCityName.charAt(0))) - 64) + 
        " (" + citiesCoords.get(newCityName) + 
        ") to the list of size " + paths.keySet().size()
      );
    if(paths.isEmpty()) {
      paths.put(newCityName, (double) 0);
      return paths;
    }

    List<String> combinations = getAllCombinations(newCityName, paths.keySet(), homeCity);

    for(String combination : combinations) {
      if(combination.length() == 2) {
        paths.put(combination,
                  calculateDistance(citiesCoords.get(String.valueOf(combination.charAt(0))),
                                    citiesCoords.get(String.valueOf(combination.charAt(1)))));
      } else if(combination.startsWith(newCityName)) {
        String firstPathName = newCityName + combination.charAt(1);
        String secondPathName = combination.substring(1);
        double val1 = paths.get(firstPathName);
        double val2 = paths.get(secondPathName);
        paths.put(combination, val1 + val2);
      } else if (combination.endsWith(newCityName)) {
        String firstPathName = combination.substring(0, combination.length() - 1);
        String secondPathName = combination.charAt(combination.length() - 2) + newCityName;
        double val1 = paths.get(firstPathName);
        double val2 = paths.get(secondPathName);
        paths.put(combination, val1 + val2);
      } else {
        String firstPathName = combination.substring(0, combination.indexOf(newCityName));
        if (firstPathName.length() == 1) {
          firstPathName = firstPathName + newCityName;
        }
        String secondPathName = combination.substring(combination.indexOf(newCityName), (combination.indexOf(newCityName) + 2));
        String thirdPathName = combination.substring(combination.indexOf(newCityName) + 1, combination.length());
        double val1 = paths.get(firstPathName);
        double val2 = paths.get(secondPathName);
        double val3 = 0;
        if(thirdPathName.length() > 1){
              val3 = paths.get(thirdPathName);
        }
        paths.put(combination, val1 + val2 + val3);
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

  private static List<String> getAllCombinations(String cityName, Set<String> paths, boolean homeCity) {
    if(paths.size() == 1) {
      paths.clear();
    }

    List<String> combinations = new ArrayList<>();

    for(int i = 65; i < cityName.charAt(0); i++) {
      combinations.add((char)i + cityName);
      combinations.add(cityName + (char)i);
    }

    List<String> cityPathsToConsider = new ArrayList<>(paths);

    if(cityPathsToConsider.size() == 1) {
      String cityPath = cityPathsToConsider.get(0);
      char[] cityPathArr = cityPath.toCharArray();
      Character m = cityPathArr[0];
      cityPathArr[0] = cityPathArr[1];
      cityPathArr[1] = m;
      cityPathsToConsider.add(new String(cityPathArr));
    }

    if(homeCity) {
      for(String cityPath : cityPathsToConsider) {
        if(!cityPath.endsWith(cityName)) {
          combinations.add(cityPath + cityName);
        }
      }
    } else {
      for(String cityPath : cityPathsToConsider) {
        combinations.add(cityPath + cityName);
        cityPath = cityName + cityPath;
        combinations.add(cityPath);
        char[] cityPathArr = cityPath.toCharArray();
        for(int i = 1; i < cityPath.length(); i++) {
          Character m = cityPathArr[i - 1];
          cityPathArr[i - 1] = cityPathArr[i];
          if (m == cityPathArr[i]) System.exit(0);
          cityPathArr[i] = m;
          combinations.add(new String(cityPathArr));
        }
      }
    }

    return combinations;
  }

  private static Map<String, Map<Double, Double>> readFile(String filename) {
    Map<String, Map<Double, Double>> map = new HashMap<>();
    List<String> coords = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = br.readLine();
      while ((line = br.readLine()) != null) {
        if(line.startsWith("//")) {
          solution = Double.valueOf(line.substring(3));
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
