import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Kosaraju {

  private static long dfsIndex;

  private static int strongComponentCount = 0;
  private static Map<Integer, Long> componentSizeMap = new HashMap<>();
  private static long accSize = 0;
  private static long totalSize = 0;

  public static void main(String[] arg) {
    if(arg.length != 1) {
      System.out.println("please give file name");
      return;
    }
    String fileName = arg[0];

    TreeMap<Long, Node> nodeMap = nodesFromFile(fileName);
    System.out.println("\nloaded from file");
    totalSize = nodeMap.values().size();
    System.out.println(totalSize + " graphs");

    TreeMap<Long, Node> reversedGraph = reverseGraph(nodeMap);
    System.out.println("\nreversed");
    //System.out.println(reversedGraph);
    //System.out.println(reversedGraph.get(2L));

    TreeMap<Long, Node> firstDfs = dfs(reversedGraph);
    System.out.println("\n\nfirst dfs");
    //System.out.println(firstDfs);
    System.out.println("Component count");
    System.out.println(strongComponentCount);
    strongComponentCount = 0;
    dfsIndex = 0;
    accSize = 0;

    TreeMap<Long, Node> reversedIndexes = reverseIndexes(firstDfs);
    System.out.println("\n reversed indexes");
    System.out.println(reversedIndexes);

    TreeMap<Long, Node> reversedDfsGraph = reverseGraph(reversedIndexes);
    System.out.println("\n reversed dfs graph");
    System.out.println(reversedDfsGraph);

    TreeMap<Long, Node> secondDfs = dfs(reversedDfsGraph);
    System.out.println("\n second dfs");
    System.out.println(secondDfs);

    System.out.println("Component count");
    System.out.println(strongComponentCount);

    System.out.println("\n\n");
    System.out.println(componentSizeMap
        .values()
        .stream()
        .sorted(Collections.reverseOrder())
        .limit(5)
        .collect(Collectors.toList()));
  }

  private static TreeMap<Long, Node> reverseIndexes(TreeMap<Long, Node> graph) {
    //System.out.println("\nreverse indexes...");
    TreeMap<Long, Node> reversedIndexMap = new TreeMap<>();

    for(Map.Entry<Long, Node> entry : graph.entrySet()) {
      Node node = entry.getValue();
      node.setNodeIndex(node.getDfsIndex());
      node.setDfsIndex(null);
      node.setExplored(false);

      reversedIndexMap.put(node.getNodeIndex(), node);
    }

    return reversedIndexMap;
  }

  private static TreeMap<Long, Node>  dfs(TreeMap<Long, Node> graph) {
    System.out.println("\ndfs...");
    dfs(graph, new Stack<>(), new TreeSet<>());
    return graph;
  }

  private static void dfs(TreeMap<Long, Node> map, Stack<Node> nodeStack, TreeSet<Long> explNodeIdSet) {

      while(explNodeIdSet.size() < map.values().size()) {
        nodeStack.push(map.get(getNextNonExplored(map, explNodeIdSet)));
        explNodeIdSet.add(nodeStack.peek().getNodeIndex());

        while (!nodeStack.isEmpty()) {
          System.out.println(dfsIndex);
          Node node = nodeStack.pop();
          List<Node> connectedNodes = node.getConnectedNodes().stream().filter(n -> !explNodeIdSet.contains(n.getNodeIndex())).collect(Collectors.toList());

          if(connectedNodes.isEmpty()) {
            node.setDfsIndex(++dfsIndex);
            continue;
          } else {
            nodeStack.push(node);
            connectedNodes.forEach(n -> {
              nodeStack.push(n);
              explNodeIdSet.add(n.getNodeIndex());
            });
          }
        }

        strongComponentCount++;
        if(componentSizeMap.isEmpty()) {
          componentSizeMap.put(strongComponentCount, Long.valueOf(explNodeIdSet.size()));
        } else {
          componentSizeMap.put(strongComponentCount, explNodeIdSet.size() - accSize);
        }
        accSize = explNodeIdSet.size();
      }
  }
  
  private static Long getNextNonExplored(TreeMap<Long, Node> map, TreeSet<Long> explNodeIdSet) {
    NavigableSet<Long> nSet = ((NavigableSet) map.keySet()).descendingSet();

    for(Long id : nSet) {
      if(!explNodeIdSet.contains(id)) {
        return id;
      }
    }
    return 0L;
  }

  private static TreeMap<Long, Node> reverseGraph(Map<Long, Node> graph) {
    System.out.println("\nreverse graph...");
    //System.out.println(graph);
    TreeMap<Long, Node> reversedGraph = new TreeMap<>();

    for(Node node : graph.values()) {
      List<Node> connectedNodeList = node.getConnectedNodes();
      Node parentNode = reversedGraph.get(node.getNodeIndex());
      if(parentNode == null) {
        parentNode = new Node(node.getNodeIndex());
        parentNode.setDfsIndex(node.getDfsIndex());
        reversedGraph.put(parentNode.getNodeIndex(), parentNode);
      }

      for(int i = 0; i < connectedNodeList.size(); i++) {
        Node connectedNode = connectedNodeList.get(i);

        Node childNode = reversedGraph.getOrDefault(connectedNode.getNodeIndex(), new Node(connectedNode.getNodeIndex()));

        childNode.connectNode(parentNode);
        childNode.setDfsIndex(connectedNode.getDfsIndex());
        reversedGraph.put(childNode.getNodeIndex(), childNode);
      }
    }

    return reversedGraph;
  }

  private static TreeMap<Long, Node> nodesFromFile(String fileName) {
    TreeMap<Long, Node> nodeMap = new TreeMap<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;

      String separator = String.valueOf(" ");
      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        String[] parsedLine = currentLine.split(separator);
        Long nodeId = Long.valueOf(parsedLine[0]);
        Long connectedNodeId = Long.valueOf(parsedLine[1]);

        Node node = nodeMap.getOrDefault(nodeId, new Node(nodeId));
        nodeMap.put(node.getNodeId(), node);

        Node connectedNode = nodeMap.getOrDefault(connectedNodeId, new Node(connectedNodeId));
        node.connectNode(connectedNode);
        nodeMap.put(connectedNode.getNodeId(), connectedNode);
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return nodeMap;
  }
}
