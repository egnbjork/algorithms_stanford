import java.util.*;
import java.util.stream.*;
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

    Map<Integer, Integer> shortestPaths = getShortestPaths(fileMap);
    System.out.println(shortestPaths);
    if(shortestPaths.size() == 200) {
      System.out.print("\n\n" + shortestPaths.get(7) + ",");
      System.out.print(shortestPaths.get(37) + ",");
      System.out.print(shortestPaths.get(59) + ",");
      System.out.print(shortestPaths.get(82) + ",");
      System.out.print(shortestPaths.get(99) + ",");
      System.out.print(shortestPaths.get(115) + ",");
      System.out.print(shortestPaths.get(133) + ",");
      System.out.print(shortestPaths.get(165) + ",");
      System.out.print(shortestPaths.get(188) + ",");
      System.out.print(shortestPaths.get(197));
    }
  }

  public static Map<Integer, Integer> getShortestPaths(Map<Integer, Map<Integer, Integer>> fileMap) {
    Map<Integer, Integer> shortestPathsMap = new TreeMap<>();
    //start from first element
    Map<Integer, Integer> heapMap = new LinkedHashMap<>();
    heapMap.put(1,0);
    shortestPathsMap.put(1,0);

    while(!heapMap.isEmpty()) {
      Map.Entry<Integer, Integer> vx = heapMap.entrySet().iterator().next();
      //System.out.println("vx: " + vx);
      heapMap.remove(vx.getKey());

      Integer vxPath = shortestPathsMap.get(vx.getKey());

      Map<Integer, Integer> paths = fileMap.get(vx.getKey()).entrySet()
        .stream()
        .collect(Collectors.toMap(e -> e.getKey(), 
              e -> vxPath + e.getValue()));

      //System.out.println(paths);
      for(Map.Entry<Integer, Integer> path : paths.entrySet()) {

        if(!shortestPathsMap.containsKey(path.getKey()) ||
            shortestPathsMap.get(path.getKey()) > path.getValue()) {
          heapMap.put(path.getKey(), path.getValue());
          shortestPathsMap.put(path.getKey(), path.getValue());
        }
      }

      //System.out.println("heap: " + heapMap);
      //System.out.println("shortest paths: " + shortestPathsMap);
    }

    return shortestPathsMap;
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
