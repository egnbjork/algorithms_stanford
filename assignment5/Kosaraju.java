import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Kosaraju {

  private static int dfsIndex;

  public static void main(String[] arg) {
    if(arg.length != 1) {
      System.out.println("please give file name");
      return;
    }
    String fileName = arg[0];

    TreeMap<Long, Node> nodeMap = nodesFromFile(fileName);
    System.out.println(nodeMap);
    TreeMap<Long, Node> reversedMap = reverseGraph(nodeMap);
    System.out.println("reversed");
    System.out.println(reversedMap);
    TreeMap<Long, Node> reversedDfsMap = reverseDfs(reversedMap);
    System.out.println("reversed dfs");
    System.out.println(reversedDfsMap);
    //recurse from the biggest
    // reverse nodes + change indexes
    // recurse from the biggest
  }

  private static TreeMap<Long, Node>  reverseDfs(TreeMap<Long, Node> graph) {
    TreeMap<Long, Node> reversedDfs = new TreeMap<>();
    dfs(graph, new Stack<>(), new TreeSet<>());

    //System.out.println("reversed graph: " + reversedDfs);
    return reversedDfs;
  }

  private static long dfs(TreeMap<Long, Node> map, Stack<Node> nodeStack, TreeSet<Long> explNodeIdSet) {

    if(nodeStack.isEmpty() && explNodeIdSet.isEmpty()) {
      Node node = map.lastEntry().getValue();
      node.setExplored(true);
      nodeStack.push(node);
      System.out.println("explored " + node.getNodeId());
      explNodeIdSet.add(node.getNodeId());
    } else if(nodeStack.isEmpty()) {
      System.out.println("BOO");
      System.out.println(map);
      System.out.println(explNodeIdSet);
      if(explNodeIdSet.size() != map.keySet().size()) {
        System.out.println("MOOHOO");
        Long nextNonExplored = getNextNonExplored(map, explNodeIdSet);
        nodeStack.push(map.get(nextNonExplored));
        System.out.println("explored " + nextNonExplored);
        explNodeIdSet.add(nextNonExplored);
      } else {
        return explNodeIdSet.first();
      }
    }
    
    Node node = map.get(nodeStack.pop().getNodeId());

    node.setExplored(true);
    System.out.println("explored " + node.getNodeId());
    explNodeIdSet.add(node.getNodeId());
    nodeStack.push(node);

    List<Node> connectedNodes = node
      .getConnectedNodes()
      .stream()
      .filter(n -> !n.isExplored())
      .filter(n -> !explNodeIdSet.contains(n.getNodeId()))
      .collect(Collectors.toList());
    System.out.println("===============================>>>>>>>>");
    System.out.println(connectedNodes);

    if(connectedNodes.isEmpty()) {
      System.out.println(nodeStack);
      System.out.println("===>ME");
      System.out.println(node);
      if(node.getDfsIndex() == null) {
        node.setDfsIndex(Long.valueOf(++dfsIndex));
        System.out.print("========>dfsIndex ");
        System.out.println(dfsIndex);
      }
      nodeStack.pop();
      dfs(map, nodeStack, explNodeIdSet);
      return node.getNodeId();
    }

    connectedNodes.forEach(n -> nodeStack.push(n));
    nodeStack.peek().setExplored(true);

    System.out.print("node " + node.getNodeId() + " has " + connectedNodes.size() + " connected nodes " + connectedNodes);
    System.out.println("------- stack" + nodeStack);

    dfs(map, nodeStack, explNodeIdSet);
    return explNodeIdSet.first();
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
    TreeMap<Long, Node> reversedGraph = new TreeMap<>();

    for(Node node : graph.values()) {
      List<Node> connectedNodeList = node.getConnectedNodes();
      //System.out.println("node " + node.getNodeId());
      for(int i = 0; i < connectedNodeList.size(); i++) {
        Node connectedNode = connectedNodeList.get(i);

        Node reversedNode = reversedGraph.getOrDefault(connectedNode.getNodeId(), new Node(connectedNode.getNodeId()));
//        System.out.println("reversedNode= " + reversedNode);

        reversedNode.connectNode(node);
        reversedGraph.put(reversedNode.getNodeId(), reversedNode);

//        System.out.println("connected node " + connectedNode.getNodeId());
 //       System.out.print("===graph ");
 //      System.out.println(graph);
//        System.out.print("===reversed ");
//        System.out.println(reversedGraph);
//        System.out.println("");
//        System.out.println("");
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
