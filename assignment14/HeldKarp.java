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
        List<String> citiesCoords = readFile(args[0]);
        System.out.println(citiesCoords);
  }

    private static List<String> readFile(String filename) {
      List<String> coords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
              coords.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coords;
    }
}
