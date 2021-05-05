import java.util.*;
import java.util.stream.*;

public class Node {
  private Long nodeId;
  private List<Node> connectedNodeList;

  private boolean isExplored;
  private boolean isReversed;
  private boolean isDfs;
  private Long dfsIndex;

  public Node(Long nodeId) {
    this.nodeId = nodeId;
    connectedNodeList = new ArrayList<>();
  }
  
  public Long getNodeId() {
    return nodeId;
  }

  public List<Node> getConnectedNodes() {
    return connectedNodeList;
  }

  public void connectNode(Node node) {
    connectedNodeList.add(node);
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

  @Override
  public String toString() {
    return "Id " + nodeId + " connected to " + connectedNodeList.stream().map(n -> n.getNodeId()).collect(Collectors.toList());
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
