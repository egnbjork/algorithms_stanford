import java.util.*;
import java.util.stream.*;

public class Node implements Comparable<Node> {
  private Long nodeId;
  private List<Node> connectedNodeList;

  private boolean isExplored;
  private Long nodeIndex;
  private Long dfsIndex;

  public Node(Long nodeId) {
    this.nodeId = nodeId;
    this.nodeIndex = nodeId;
    connectedNodeList = new ArrayList<>();
  }

  public Long getNodeId() {
    return nodeId;
  }

  public void setNodeIndex(Long index) {
    this.nodeIndex = index;
  }

  public List<Node> getConnectedNodes() {
    return connectedNodeList;
  }
  
  public Long getNodeIndex() {
    return nodeIndex;
  }

  public void connectNode(Node node) {
    connectedNodeList.add(node);
  }

  public void disconnectNode(Node node) {
    connectedNodeList.remove(node);
  }

  public Long getDfsIndex() {
    return dfsIndex;
  }

  public void setDfsIndex(Long index) {
    dfsIndex = index;
  }

  public boolean isExplored() {
    return this.isExplored;
  }

  public void setExplored(boolean value) {
    this.isExplored = value;
  }

  @Override
  public int compareTo(Node node) {
    return this.getNodeId().compareTo(node.getNodeId());
  }

  @Override
  public String toString() {
    return "Id " + getNodeId() + 
      " nodeIndex " + nodeIndex + " " +
         (dfsIndex == null ? "" : "(dfsIndex: " + dfsIndex.toString() + ")") +
         " connected to " + connectedNodeList.stream().map(n -> n.nodeIndex + " id " + n.nodeId + " index " + n.nodeIndex + " dfs " + n.dfsIndex).collect(Collectors.toList()) + (isExplored() ? " E" : "") + "\n";
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == this) {
      return true;
    }

    if(!(obj instanceof Node)) {
      return false;
    }

    return this.nodeIndex.equals(((Node) obj).getNodeIndex());
  }

  @Override
  public int hashCode() {
    return this.nodeId.hashCode();
  }
}
