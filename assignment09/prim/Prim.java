import java.io.*;
import java.util.*;

public class Prim {
  public static void main(String args[]) {
    if(args.length < 1) {
      System.out.println("not enough args are given");
      return;
    }

    TreeSet<EdgeWeight> treeSet = readFile(args[0]);
    System.out.println(treeSet);

    System.out.println("min cost is " + getMinimumCost(treeSet));
  }

  private static long getMinimumCost(TreeSet<EdgeWeight> treeSet) {
    Set<Integer> calcEdges = new TreeSet<>();
    long cost = 0;
    calcEdges.add(treeSet.first().e1);
    while(!treeSet.isEmpty()) {
      Iterator<EdgeWeight> it = treeSet.iterator();
      while(it.hasNext()) {
        EdgeWeight ew = it.next();
        System.out.println(ew);
        if(calcEdges.contains(ew.e1) && calcEdges.contains(ew.e2)) {
          treeSet.remove(ew);
          break;
        } else if(!calcEdges.contains(ew.e1) && !calcEdges.contains(ew.e2)) {
          continue;
        } else {
          calcEdges.add(ew.e1);
          calcEdges.add(ew.e2);
          cost += ew.weight;
        }

        it = treeSet.iterator();
      }
    }
    return cost;
  }

  private static TreeSet<EdgeWeight> readFile(String fileName) {
    TreeSet<EdgeWeight> treeSet = new TreeSet<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;
        String[] data = currentLine.split(" ");
        Integer e1 = Integer.valueOf(data[0]);
        Integer e2 = Integer.valueOf(data[1]);
        Long weight = Long.valueOf(data[2]);
        EdgeWeight edgeWeight = new EdgeWeight(e1, e2, weight);
        System.out.println(edgeWeight);
        treeSet.add(edgeWeight);
        System.out.println(treeSet);

      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return treeSet;
  }

  private static class EdgeWeight implements Comparable<EdgeWeight> {
    private Long weight;
    private Integer e1;
    private Integer e2;

    public EdgeWeight(Integer e1, Integer e2, Long weight) {
      this.e1 = e1;
      this.e2 = e2;
      this.weight = weight;
    }

    @Override
    public boolean equals(Object that) {
      EdgeWeight ew = (EdgeWeight) that;
      return this.e1.equals(ew.e1) &&
        this.e2.equals(ew.e2) &&
        this.weight.equals(ew.weight);
    }

    @Override 
    public int hashCode() {
      return weight.hashCode() + e1.hashCode() + e2.hashCode();
    }

    @Override
    public int compareTo(EdgeWeight other) {
      if(this.weight.equals(other.weight)) {
        if(this.e1.equals(other.e1)) {
          return this.e2.compareTo(other.e2);
        }
        return this.e1.compareTo(other.e1);
      }
      return this.weight.compareTo(other.weight);
    }

    @Override
    public String toString() {
      return weight + " " + e1 + "," + e2;
    }
  }
}
