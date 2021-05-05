import java.util.*;

public class Graph {
  private Map<Integer, List<Integer>> adjVertices;

  public Graph() {
    this.adjVertices = new HashMap<>();
  }

  public Map.Entry<Integer, List<Integer>> pollRandomVertex() {
    List<Integer> keys = new ArrayList<>(adjVertices.keySet());
    Integer randomNumber = new Random().nextInt(keys.size());
    Integer randomKey = keys.toArray(new Integer[keys.size()])[randomNumber];;
    Map.Entry<Integer, List<Integer>> entry = adjVertices.entrySet().stream().filter(n -> n.getKey().equals(randomKey)).findFirst().get();
    adjVertices.remove(randomKey);

    return entry;
  }

  public Map.Entry<Integer, List<Integer>> pollRandomVertex(List<Integer> vertexList) {
    List<Integer> searchList = new ArrayList<>(vertexList);
    while(searchList.size() > 0) {
      Integer randomKey = searchList.get(new Random().nextInt(searchList.size()));
      searchList.remove(randomKey);
      if(adjVertices.containsKey(randomKey)) {
        Map.Entry<Integer, List<Integer>> entry = adjVertices.entrySet().stream().filter(n -> n.getKey().equals(randomKey)).findFirst().get();
        adjVertices.remove(randomKey);
        return entry;
      }
    }

    return null;
  }

  public void fillVertex(Integer label, List<Integer> row) {
    row.remove(label);
    adjVertices.put(label, row);
  }

  public void addVertex(Integer key, List<Integer> value) {
    adjVertices.put(key, value);
  }

  public List<Integer> getVertex(Integer number) {
    return adjVertices.get(number);
  }

  public void removeVertex(Integer number) {
    adjVertices.remove(number);
  }

  public Map<Integer, List<Integer>> getGraph() {
    return adjVertices;
  }

  public String toString() {
    return adjVertices.toString();
  }
}
