import java.util.*;
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

        //Map<String, Double> distanceTable = calcCityDistance(citiesCoords);
  }

    private static Map<String, Double> calcCityDistance(List<String> citiesCoords) {
        Map<String, Double> distanceTable = new HashMap<>();
        for (int i = 0; i < citiesCoords.size(); i++) {
          Character cityName = (char) (65 + i);
          distanceTable.put(String.valueOf(cityName), Double.valueOf(0));
        }

        System.out.println(distanceTable);
        return distanceTable;
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

        System.out.println(map);
        return map;
    }
}
