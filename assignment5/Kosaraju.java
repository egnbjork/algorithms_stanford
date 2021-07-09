import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Kosaraju {

  private static int dfsIndex;

  private static int strongComponentCount = 1;

  public static void main(String[] arg) {
    if(arg.length != 1) {
      System.out.println("please give file name");
      return;
    }
    String fileName = arg[0];

    TreeMap<Long, Node> nodeMap = nodesFromFile(fileName);
    System.out.println("\nloaded from file");
    System.out.println(nodeMap);

    TreeMap<Long, Node> first = reverseGraph(nodeMap);
    System.out.println("\nreversed");
    System.out.println(first);

    TreeMap<Long, Node> second = dfs(first);
    System.out.println("\n\nfirst dfs");
    System.out.println(second);
    System.out.println("Component count");
    System.out.println(strongComponentCount);
    strongComponentCount = 1;
    dfsIndex = 0;

    TreeMap<Long, Node> third = reverseIndexes(second);
    System.out.println("\n reversed nodes");
    System.out.println(third);

    TreeMap<Long, Node> fourth = dfs(third);
    System.out.println("\n second dfs");
    System.out.println(fourth);

    System.out.println("Component count");
    System.out.println(strongComponentCount);
  }

  private static TreeMap<Long, Node> reverseIndexes(TreeMap<Long, Node> graph) {
    System.out.println("\nreverse indexes...");
    TreeMap<Long, Node> reversedIndexMap = new TreeMap<>();

    for(Map.Entry<Long, Node> entry : graph.entrySet()) {
      Node node = entry.getValue();
      node.reverse();
      node.setExplored(false);

      System.out.println(node);
      reversedIndexMap.put(node.getNodeId(), node);
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
      Node node = map.lastEntry().getValue();
      node.setExplored(true);
      nodeStack.push(node);
      explNodeIdSet.add(node.getNodeId());
    } else if(nodeStack.isEmpty()) {
      if(explNodeIdSet.size() != map.keySet().size()) {
        Long nextNonExplored = getNextNonExplored(map, explNodeIdSet);
        strongComponentCount++;
        nodeStack.push(map.get(nextNonExplored));
        explNodeIdSet.add(nextNonExplored);
      } else {
        return explNodeIdSet.first();
      }
    }

    Node node = map.get(nodeStack.pop().getNodeId());

    node.setExplored(true);
    explNodeIdSet.add(node.getNodeId());
    nodeStack.push(node);

    List<Node> connectedNodes = node
      .getConnectedNodes()
      .stream()
      .filter(n -> !n.isExplored())
      .filter(n -> !explNodeIdSet.contains(n.getNodeId()))
      .collect(Collectors.toList());

    if(connectedNodes.isEmpty()) {
      if(node.isReversed()) {
        if(node.getReversedDfsIndex() == null) {
          node.setReversedDfsIndex(Long.valueOf(++dfsIndex));
          System.out.println(dfsIndex);
        }
      } else {
        if(node.getDfsIndex() == null) {
          node.setDfsIndex(Long.valueOf(++dfsIndex));
          System.out.println(dfsIndex);
        }
      }
      nodeStack.pop();
      dfs(map, nodeStack, explNodeIdSet);
      return node.getNodeId();
    }

    connectedNodes.forEach(n -> nodeStack.push(n));
    nodeStack.peek().setExplored(true);

    System.out.print("node " + node.getNodeId() + " has " + connectedNodes.size() + " connected nodes " + connectedNodes);

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
    System.out.println("\nreverse graph...");
    TreeMap<Long, Node> reversedGraph = new TreeMap<>();

    for(Node node : graph.values()) {
      List<Node> connectedNodeList = node.getConnectedNodes();
      for(int i = 0; i < connectedNodeList.size(); i++) {
        Node connectedNode = connectedNodeList.get(i);

        Node reversedNode = reversedGraph.getOrDefault(connectedNode.getNodeId(), new Node(connectedNode.getNodeId()));

        reversedNode.connectNode(node);
        reversedNode.setDfsIndex(node.getDfsIndex());
        reversedGraph.put(reversedNode.getNodeId(), reversedNode);

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
