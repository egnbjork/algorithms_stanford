import java.util.*;
import java.io.*;

public class Dijkstra {

  public static void main(String[] arg) {
    if(arg.length != 1) {
      System.out.println("please give file name");
      return;
    }
    String fileName = arg[0];
  
    Map<Integer, Map<Integer, Integer>> fileMap = readFile(fileName);
    System.out.println(fileMap);
  }
  
  public static Map<Integer, Map<Integer, Integer>> readFile(String fileName) {
    Map<Integer, Map<Integer, Integer>> fileMap = new TreeMap<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;

      String separator = String.valueOf("\t");
      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        String[] parsedLine = currentLine.split(separator,2);

        String[] connectedNodesArray = parsedLine[1].split("\t");

        Map<Integer, Integer> connectedNodesMap = new TreeMap<>();
        for (int i = 0; i < connectedNodesArray.length; i++) {
          String[] nodeValue = connectedNodesArray[i].split(",");
          connectedNodesMap.put(Integer.valueOf(nodeValue[0]), Integer.valueOf(nodeValue[1]));
        }

        fileMap.put(Integer.valueOf(parsedLine[0]), connectedNodesMap);

      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return fileMap;
  }
}
