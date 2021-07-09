import java.util.*;
import java.util.stream.*;

public class Node implements Comparable<Node> {
  private Long nodeId;
  private List<Node> connectedNodeList;

  private boolean isExplored;
  private boolean isReversed;
  private boolean isDfs;
  private Long nodeIndex;
  private Long dfsIndex;
  private Long reversedDfsIndex;

  public Node(Long nodeId) {
    this.nodeId = nodeId;
    this.nodeIndex = nodeId;
    connectedNodeList = new ArrayList<>();
  }
  
  public void setReversedDfsIndex(Long index) {
    this.reversedDfsIndex = index;
  }

  public Long getReversedDfsIndex() {
    return this.reversedDfsIndex;
  }
  public Long getNodeId() {
    return isReversed ? dfsIndex : nodeId;
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

  public void reverse() {
    isReversed = isReversed ? false : true;
  }

  public boolean isReversed() {
    return isReversed;
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
      (isReversed() ? " reversed (id: " + nodeId + ")" :
         (dfsIndex == null ? "" : "(dfsIndex: " + dfsIndex.toString() + ")")) +
         " connected to " + connectedNodeList.stream().map(n -> n.getNodeIndex()).collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == this) {
      return true;
    }

    if(!(obj instanceof Node)) {
      return false;
    }

    return this.nodeId.equals(((Node) obj).getNodeId());
  }

  @Override
  public int hashCode() {
    return this.nodeId.hashCode();
  }
}
