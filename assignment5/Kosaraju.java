import java.io.*;
import java.util.*;

public class Kosaraju {
  public static void main(String[] arg) {
    if(arg.length != 1) {
      System.out.println("please give file name");
      return;
    }
    String fileName = arg[0];

    Map<Long, Node> nodeMap = nodesFromFile(fileName);
    System.out.println(nodeMap);
    System.out.println("reversed");
    System.out.println(reverseGraph(nodeMap));
    //reverse nodes
    //recurse from the biggest
    // reverse nodes + change indexes
    // recurse from the biggest
  }

  private static Map<Long, Node> reverseGraph(Map<Long, Node> graph) {
    Map<Long, Node> reversedGraph = new TreeMap<>();

    for(Node node : graph.values()) {
      List<Node> connectedNodeList = node.getConnectedNodes();
      System.out.println("node " + node.getNodeId());
      for(int i = 0; i < connectedNodeList.size(); i++) {
        Node connectedNode = connectedNodeList.get(i);

        Node reversedNode = reversedGraph.getOrDefault(connectedNode.getNodeId(), new Node(connectedNode.getNodeId()));
        System.out.println("reversedNode= " + reversedNode);

        reversedNode.connectNode(node);
        reversedGraph.put(reversedNode.getNodeId(), reversedNode);

        //reserse
        //check reversed graph if exist or add new

        System.out.println("connected node " + connectedNode.getNodeId());
        System.out.print("===graph ");
        System.out.println(graph);
        System.out.print("===reversed ");
        System.out.println(reversedGraph);
        System.out.println("");
        System.out.println("");
      }
    }
    return reversedGraph;
  }

  private static Map<Long, Node> nodesFromFile(String fileName) {
    Map<Long, Node> nodeMap = new TreeMap<>();

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
