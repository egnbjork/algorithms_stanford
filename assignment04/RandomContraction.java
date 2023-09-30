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

    TreeMap<Integer, List<List<Integer>>> minCutsMap = new TreeMap<>();
    Set<Integer> minCuts = new TreeSet<>();

    for(int i = 0; i < 10000; i++) {
      System.out.println("=================");
      Graph graph = graphFromFile(fileName);
      System.out.println(graph.getGraph());

      graph = contract(graph);

      if(graph.getGraph().keySet().size() == 2) {

          Set<Map.Entry<Integer, List<Integer>>> graphEntries = graph.getGraph().entrySet();
          Iterator<Map.Entry<Integer, List<Integer>>> it = graph.getGraph().entrySet().iterator();

          Map.Entry<Integer, List<Integer>> obj = it.next();

          List<Integer> firstList = new ArrayList<>(obj.getValue());
          minCuts.add(firstList.size());
          Integer firstKey = obj.getKey();
          //firstList.remove(firstKey);
          firstList.add(firstKey);
          Collections.reverse(firstList);

          obj = it.next();
          List<Integer> secondList = new ArrayList<>(obj.getValue());
          Integer secondKey = obj.getKey();
          //secondList.remove(secondKey);
          secondList.add(secondKey);
          Collections.reverse(secondList);
          System.out.println("First list: " + firstList);
          System.out.println("Second list: " + secondList);

          List<Integer> minCutList = new ArrayList<>(obj.getValue());
          minCutList.retainAll(firstList);
          minCutList.remove(firstKey);
          minCutList.remove(secondKey);
          System.out.println("After retention: " + minCutList);

          List<List<Integer>> remainedLists = new ArrayList<>();
          remainedLists.add(firstList);
          remainedLists.add(secondList);

          minCutsMap.put(minCutList.size(), remainedLists);

          minCuts.add(minCutList.size());

          System.out.println(remainedLists);
      }
    }
    
    System.out.println("Final number is " + minCutsMap.firstEntry());

    System.out.println("min cuts count list: " + minCuts);
  }

  private static Graph contract(Graph graph) {
    System.out.println(graph);
    if(graph.getGraph().size() < 3) {
      return graph;
    };

    Map.Entry<Integer, List<Integer>> v1 = graph.pollRandomVertex();
    System.out.println("chosen v1= " + v1);

    Map.Entry<Integer, List<Integer>> v2 = graph.pollRandomVertex(v1.getValue());
    System.out.println("chosen v2= " + v2);

    if(v2 == null) {
      graph.addVertex(v1.getKey(), v1.getValue());
      return graph;
    } else if(v2.getValue().isEmpty()) {
      graph.addVertex(v1.getKey(), v1.getValue());
      graph.addVertex(v2.getKey(), v2.getValue());
      return graph;
    }

    List<Integer> mergedList = new ArrayList<>();
    mergedList.addAll(v1.getValue());
    mergedList.addAll(v2.getValue());
    mergedList.remove(v2.getKey());
    //mergedList.remove(v1.getKey());
    //mergedList.add(v2.getKey());
    graph.addVertex(v1.getKey(), mergedList);
    System.out.println("after merge " + v2.getKey() + "= " + graph.getGraph().get(v1.getKey()));

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
          graphList.fillVertex(row.get(0), row);
        }

        currentLine = reader.readLine();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return graphList;
  }
}
