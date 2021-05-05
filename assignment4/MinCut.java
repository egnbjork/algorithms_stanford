import java.io.*;
import java.util.*;
import java.util.stream.*;

public class MinCut {
  public static void main(String[] arg) {
    if(arg.length != 1) {
      System.out.println("please give file name");
      return;
    }
    String fileName = arg[0];
    List<List<Integer>> graph = graphFromFile(fileName);

    graph.forEach(n -> n.stream().map(Object::toString).collect(Collectors.joining(" ")));
  }

  private static List<List<Integer>> graphFromFile(String fileName) {
    List<List<Integer>> graph = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine = reader.readLine();

      String separator = String.valueOf(currentLine.charAt(1));
      while(currentLine !=  null) {
        graph.add(Arrays.stream(currentLine.trim().split(separator))
          .filter(n -> !n.isEmpty())
          .map(n -> Integer.valueOf(n))
          .collect(Collectors.toList()));

        currentLine = reader.readLine();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

        System.out.println(graph);
    return graph;
  }
}
