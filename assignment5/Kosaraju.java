import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Kosaraju {

  private static int dfsIndex;

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
    //System.out.println(reversedIndexes);

    TreeMap<Long, Node> reversedDfsGraph = reverseGraph(reversedIndexes);
    System.out.println("\n reversed dfs graph");
    //System.out.println(reversedDfsGraph);

    TreeMap<Long, Node> secondDfs = dfs(reversedDfsGraph);
    System.out.println("\n second dfs");
    //System.out.println(secondDfs);

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

  private static long dfs(TreeMap<Long, Node> map, Stack<Node> nodeStack, TreeSet<Long> explNodeIdSet) {

    if(nodeStack.isEmpty() && explNodeIdSet.isEmpty()) {
      //initialization
      //put biggest node to queue
      nodeStack.push(map.lastEntry().getValue());
      //System.out.println("starts with " + nodeStack.peek().getNodeIndex());
    } else if(nodeStack.isEmpty()) {
      //System.out.println("!!!");
      strongComponentCount++;
      if(componentSizeMap.isEmpty()) {
        componentSizeMap.put(strongComponentCount, Long.valueOf(explNodeIdSet.size()));
      } else {
        componentSizeMap.put(strongComponentCount, explNodeIdSet.size() - accSize);
      }

      if(explNodeIdSet.size() != map.keySet().size()) {
        Long nextNonExplored = getNextNonExplored(map, explNodeIdSet);
        nodeStack.push(map.get(nextNonExplored));
        explNodeIdSet.add(nextNonExplored);
      } else {
        return explNodeIdSet.first();
      }
    }

    //get last node from the queue
    Node node = map.get(nodeStack.pop().getNodeIndex());
    //System.out.println("goes to " + node.getNodeIndex());
    //map as explored
    node.setExplored(true);
    //add to explored node set
    explNodeIdSet.add(node.getNodeIndex());

    //get all connected non-explored nodes
    //List<Node> connectedNodes = node
      //.getConnectedNodes()
      //.stream()
      //.filter(n -> !n.isExplored())
      //.filter(n -> !explNodeIdSet.contains(n.getNodeIndex()))
      //.collect(Collectors.toList());
    //System.out.println(connectedNodes.size() + " non-explored nodes");

    //if no connected nodes found
    if(getConnectedUnexploredNodes(node, explNodeIdSet).isEmpty()) {
      //System.out.println("no connected nodes found");
      //if empty dfs index, put one
      if(node.getDfsIndex() == null) {
        node.setDfsIndex(Long.valueOf(++dfsIndex));
        //System.out.println("dfs increased " + dfsIndex);
        //System.out.println(node);
      }
    } else {

      //if connected nodes found
      //put node and connected nodes to stack
      //System.out.println("connected nodes found");
      nodeStack.push(node);
      getConnectedUnexploredNodes(node, explNodeIdSet).forEach(n -> nodeStack.push(n));
    }

    //recurse
    dfs(map, nodeStack, explNodeIdSet);
    return explNodeIdSet.first();
  }
  
  private static List<Node> getConnectedUnexploredNodes(Node node, Set<Long> explNodeIdSet) {
    return node
      .getConnectedNodes()
      .stream()
      .filter(n -> !n.isExplored())
      .filter(n -> !explNodeIdSet.contains(n.getNodeIndex()))
      .collect(Collectors.toList());
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
