import java.util.*;
import java.util.stream.*;
import java.io.*;

public class RandomContraction {

  public static void main(String[] arg) {
    if(arg.length != 1) {
      System.out.println("please give file name");
      return;
    }
    String fileName = arg[0];

    Set<Integer> minCuts = new TreeSet<>();

    for(int i = 0; i < 1000; i++) {
      Graph graph = graphFromFile(fileName);
      System.out.println(graph.getGraph());

      graph = contract(graph);

      if(graph.getGraph().keySet().size() == 2) {

          Set<Map.Entry<Integer, Set<Integer>>> graphEntries = graph.getGraph().entrySet();
          Iterator<Map.Entry<Integer, Set<Integer>>> it = graph.getGraph().entrySet().iterator();

          Map.Entry<Integer, Set<Integer>> obj = it.next();

          List<Integer> firstList = new ArrayList<>(obj.getValue());
          minCuts.add(firstList.size());
          Integer firstKey = obj.getKey();
          firstList.add(firstKey);

          obj = it.next();
          List<Integer> secondList = new ArrayList<>(obj.getValue());
          Integer secondKey = obj.getKey();
          minCuts.add(secondList.size());
          System.out.println(secondList.size());
      }
    }
    
    System.out.println("min cuts list: " + minCuts);

    System.out.println("Final number is " + minCuts.iterator().next());
  }

  private static Graph contract(Graph graph) {
    if(graph.getGraph().size() < 3) {
      return graph;
    };

    Map.Entry<Integer, Set<Integer>> v1 = graph.pollRandomVertex();
    System.out.println("chosen v1= " + v1);

    Map.Entry<Integer, Set<Integer>> v2 = graph.pollRandomVertex(v1.getValue());
    System.out.println("chosen v2= " + v2);

    if(v2 == null) {
      return graph;
    } else if(v2.getValue().isEmpty()) {
      graph.addVertex(v1.getKey(), v1.getValue());
      graph.addVertex(v2.getKey(), v2.getValue());
      return graph;
    }

    List<Integer> mergedList = new ArrayList<>();
    mergedList.addAll(v1.getValue());
    mergedList.addAll(v2.getValue());
    mergedList.remove(v1.getKey());
    graph.addVertex(v1.getKey(), new HashSet<>(mergedList));
    System.out.println("after merge " + v1.getKey() + "= " + graph.getGraph().get(v1.getKey()));

    return contract(graph);
  }

  private static Graph graphFromFile(String fileName) {
    Graph graphList = new Graph();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine = reader.readLine();

      String separator = String.valueOf(currentLine.charAt(1));
      while(currentLine !=  null) {
        List<Integer> row = Arrays.stream(currentLine.trim().split(separator))
          .filter(n -> !n.isEmpty())
          .map(n -> Integer.valueOf(n))
          .collect(Collectors.toList());  

        if(row.size() > 1) {
          graphList.fillVertex(row.get(0), new HashSet<>(row));
        }

        currentLine = reader.readLine();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return graphList;
  }
}
